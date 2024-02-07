output "backend_ip" {
  value = azurerm_container_group.ix-backend.ip_address
}