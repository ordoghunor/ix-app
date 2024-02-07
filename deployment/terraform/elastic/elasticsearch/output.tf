output "elasticsearch_ip" {
  value = azurerm_container_group.elasticsearch-cg.ip_address
}