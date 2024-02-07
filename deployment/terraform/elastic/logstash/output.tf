output "logstash_ip" {
  value = azurerm_container_group.logstash-cg.ip_address
}