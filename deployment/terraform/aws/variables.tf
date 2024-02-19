variable "aws_region" {
  description = "AWS region to launch stuff"
  type        = string
  sensitive   = false
  default     = "eu-west-3"
}

variable "ec2_ami" {
  description = "AWS AMI for EC2 instances"
  type        = map(string)
  default = {
    "eu-west-3" = "ami-00dd995cb6f0a5219"
  }
}

variable "instance_type" {
  description = "AWS EC2 tier"
}

variable "key_pair_name" {
}
variable "public_key_path" {
}
variable "private_key_path" {
}
