variable "database_url" {
  description = "Database connection URL"
  type        = string
  default     = "jdbc:mysql://localhost:3306/ix"
}

variable "container_image" {
  description = "Docker container image for the backend"
  type        = string
  default     = "docker.io/ordoghunor/ix-spring:0.6.4"
}