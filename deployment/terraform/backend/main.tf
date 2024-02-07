provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "ix-app-backend" {
  name     = "ix-app-backend"
  location = "germanywestcentral"
}

resource "azurerm_container_group" "ix-backend" {
  name                = "ix-backend"
  location            = azurerm_resource_group.ix-app-backend.location
  resource_group_name = azurerm_resource_group.ix-app-backend.name
  restart_policy = "Never"

  os_type = "Linux"

  container {
    name   = "ix-backend"
    image  = var.container_image

    ports {
      port     = 8080
      protocol = "TCP"
    }

    environment_variables = {
      DATABASE_URL = var.database_url
      LOGSTASH_HOST = "4.245.82.251"
    }

    cpu    = 1.0
    memory = 1.5
  }
}
