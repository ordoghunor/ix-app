provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "ix-app-monitoring" {
  name     = "ix-app-monitoring"
  location = "germanywestcentral"
}

resource "azurerm_storage_account" "ixprometheusstorage" {
  name                     = "ixprometheusstorage"
  resource_group_name      = azurerm_resource_group.ix-app-monitoring.name
  location                 = azurerm_resource_group.ix-app-monitoring.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_storage_share" "prometheus-config" {
  name                 = "prometheus-config"
  storage_account_name = azurerm_storage_account.ixprometheusstorage.name
  quota                = 1
  access_tier          = "Hot"
}

resource "azurerm_storage_share_file" "prometheus-file" {
  name             = "prometheus.yml"
  storage_share_id = azurerm_storage_share.prometheus-config.id
  source = "./prometheus.yml"
}

resource "azurerm_container_group" "ix-monitoring" {
  name                = "ix-monitoring"
  location            = azurerm_resource_group.ix-app-monitoring.location
  resource_group_name = azurerm_resource_group.ix-app-monitoring.name
  depends_on          = [azurerm_storage_share_file.prometheus-file]
  os_type = "Linux"

  container {
    name   = "ix-prometheus"
    image  = "prom/prometheus:v2.44.0"

    ports {
      port     = 9090
      protocol = "TCP"
    }

    cpu    = 0.5
    memory = 1.0

    volume {
      mount_path = "/etc/prometheus"
      name       = "config-volume"
      storage_account_name = azurerm_storage_account.ixprometheusstorage.name
      storage_account_key = azurerm_storage_account.ixprometheusstorage.primary_access_key
      share_name = azurerm_storage_share.prometheus-config.name
    }
  }

  container {
    name   = "ix-grafana"
    image  = "grafana/grafana:9.5.2"

    ports {
      port     = 3000
      protocol = "TCP"
    }

    environment_variables = {
      GF_SECURITY_ADMIN_PASSWORD = "adminx"
      GF_USERS_ALLOW_SIGN_UP = false
      PROMETHEUS_URL = "ix-prometheus"
    }

    cpu    = 0.5
    memory = 1.0
  }
}
