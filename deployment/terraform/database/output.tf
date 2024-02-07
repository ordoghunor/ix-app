output "database_ip" {
  value = azurerm_container_group.ix-mysql.ip_address
}