provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "ix-app-elastic" {
  name     = "ix-app-elastic"
  location = "westeurope"
}

resource "azurerm_storage_account" "ixelasticstorage" {
  name                     = "ixelasticstorage"
  resource_group_name      = azurerm_resource_group.ix-app-elastic.name
  location                 = azurerm_resource_group.ix-app-elastic.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
  depends_on = [azurerm_resource_group.ix-app-elastic]
}

resource "azurerm_storage_share" "elastic-config" {
  name                 = "elastic-config"
  storage_account_name = azurerm_storage_account.ixelasticstorage.name
  quota                = 1
  access_tier          = "Hot"
  depends_on = [azurerm_storage_account.ixelasticstorage]
}

resource "azurerm_storage_share_file" "elastic-config" {
  name             = "elasticsearch.yml"
  storage_share_id = azurerm_storage_share.elastic-config.id
  source = "./config/elasticsearch.yml"
  depends_on = [azurerm_storage_account.ixelasticstorage,azurerm_storage_share.elastic-config, azurerm_storage_account.ixelasticstorage]
}

resource "azurerm_storage_share_file" "jvmoptions" {
  name             = "jvm.options"
  storage_share_id = azurerm_storage_share.elastic-config.id
  source = "./config/jvm.options"
  depends_on = [azurerm_storage_account.ixelasticstorage,azurerm_storage_share.elastic-config, azurerm_storage_share_file.elastic-config]
}

resource "azurerm_storage_share_file" "log4j" {
  name             = "log4j2.properties"
  storage_share_id = azurerm_storage_share.elastic-config.id
  source = "./config/log4j2.properties"
  depends_on = [azurerm_storage_account.ixelasticstorage,azurerm_storage_share.elastic-config, azurerm_storage_share_file.elastic-config]
}

resource "azurerm_container_group" "elasticsearch-cg" {
  location            = azurerm_resource_group.ix-app-elastic.location
  name                = "elasticsearch-cg"
  os_type             = "Linux"
  resource_group_name = azurerm_resource_group.ix-app-elastic.name
  depends_on = [
    azurerm_storage_account.ixelasticstorage,
    azurerm_storage_share_file.elastic-config,
    azurerm_storage_share_file.jvmoptions,
    azurerm_storage_share_file.log4j,
    azurerm_storage_share.elastic-config
  ]
  restart_policy = "Never"
  container {
    cpu    = 0.5
    image  = "docker.elastic.co/elasticsearch/elasticsearch:8.3.3"
    memory = 1.5
    name   = "elasticsearch"
    ports {
      port     = 9200
      protocol = "TCP"
    }
    environment_variables = {
      TINI_SUBREAPER = true
    }

    volume {
      mount_path = "/usr/share/elasticsearch/config"
      name       = "elastic-config"
      storage_account_name = azurerm_storage_account.ixelasticstorage.name
      storage_account_key = azurerm_storage_account.ixelasticstorage.secondary_access_key
      share_name = azurerm_storage_share.elastic-config.name
    }
  }
}

