provider "azurerm" {
  features {}
}

resource "azurerm_storage_account" "ixlogstashstorage" {
  name                     = "ixlogstashstorage"
  resource_group_name      = "ix-app-elastic"
  location                 = "westeurope"
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_storage_share" "logstash-config" {
  name                 = "logstash-config"
  storage_account_name = azurerm_storage_account.ixlogstashstorage.name
  quota                = 1
  access_tier          = "Hot"
  depends_on = [azurerm_storage_account.ixlogstashstorage]
}

resource "azurerm_storage_share" "logstash-pipeline" {
  name                 = "logstash-pipeline"
  storage_account_name = azurerm_storage_account.ixlogstashstorage.name
  quota                = 1
  access_tier          = "Hot"
  depends_on = [azurerm_storage_account.ixlogstashstorage]
}

resource "azurerm_storage_share_file" "logstash-config" {
  name             = "logstash.yml"
  storage_share_id = azurerm_storage_share.logstash-config.id
  source = "./config/logstash.yml"
  depends_on = [azurerm_storage_share.logstash-config]
}

resource "azurerm_storage_share_file" "logstash-pipeline" {
  name             = "logstash.conf"
  storage_share_id = azurerm_storage_share.logstash-pipeline.id
  source = "./pipeline/logstash.conf"
  depends_on = [azurerm_storage_share.logstash-pipeline]
}

resource "azurerm_container_group" "logstash-cg" {
  location            = "westeurope"
  name                = "logstash-cg"
  os_type             = "Linux"
  resource_group_name = "ix-app-elastic"

  depends_on = [
    azurerm_storage_share_file.logstash-config,
    azurerm_storage_share_file.logstash-pipeline
  ]

  container {
    cpu    = 0.5
    image  = "docker.elastic.co/logstash/logstash:8.3.3"
    memory = 1
    name   = "logstash"
    ports {
      port     = 5044
      protocol = "TCP"
    }
    ports {
      port     = 5000
      protocol = "TCP"
    }
    ports {
      port     = 9600
      protocol = "TCP"
    }
    environment_variables = {
      LS_JAVA_OPTS = "-Xmx256m -Xms256m"
    }
    volume {
      mount_path = "/usr/share/logstash/config"
      name       = "config-volume"
      read_only  = true
      storage_account_name = azurerm_storage_account.ixlogstashstorage.name
      storage_account_key = azurerm_storage_account.ixlogstashstorage.secondary_access_key
      share_name = azurerm_storage_share.logstash-config.name
    }

    volume {
      mount_path = "/usr/share/logstash/pipeline"
      name       = "pipeline-volume"
      read_only  = true
      storage_account_name = azurerm_storage_account.ixlogstashstorage.name
      storage_account_key = azurerm_storage_account.ixlogstashstorage.secondary_access_key
      share_name = azurerm_storage_share.logstash-pipeline.name
    }
  }
}

resource "azurerm_container_group" "kibana-cg" {
  location            = "westeurope"
  name                = "kibana-cg"
  os_type             = "Linux"
  resource_group_name = "ix-app-elastic"
  depends_on = [azurerm_container_group.logstash-cg]

  container {
    cpu    = 0.5
    image  = "docker.elastic.co/kibana/kibana:8.3.3"
    memory = 1.5
    name   = "kibana"
    environment_variables = {
      ELASTICSEARCH_URL = "http://4.245.97.236:9200"
      ELASTICSEARCH_HOSTS = "[\"http://4.245.97.236:9200\"]"
      TINI_SUBREAPER = true
    }
    ports {
      port     = 5601
      protocol = "TCP"
    }
  }
}
