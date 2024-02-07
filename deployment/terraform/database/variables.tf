variable "mysql_root_password" {
  description = "Password for the MySQL root user"
  type        = string
  default     = "23hx86GQWER4"
}

variable "mysql_database_name" {
  description = "Name of the MySQL database"
  type        = string
  default     = "ix"
}

variable "database_container_image" {
  description = "Docker container image for the backend"
  type        = string
  default     = "mysql:8.0"
}