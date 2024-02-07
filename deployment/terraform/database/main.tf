provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "ix-database" {
  name     = "ix-database"
  location = "germanywestcentral"
}

resource "azurerm_container_group" "ix-mysql" {
  name                = "ix-mysql"
  location            = azurerm_resource_group.ix-database.location
  resource_group_name = azurerm_resource_group.ix-database.name

  os_type = "Linux"

  container {
    name   = "ix-mysql"
    image  = var.database_container_image

    ports {
      port     = 3306
      protocol = "TCP"
    }

    environment_variables = {
      MYSQL_ROOT_PASSWORD = var.mysql_root_password
      MYSQL_DATABASE = var.mysql_database_name
    }

    cpu    = 1.0
    memory = 1.0
  }
}
