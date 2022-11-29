terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "4.40.0"
    }
  }
  backend "s3" {
    bucket = "analytics-1016"
    key    = "analytics-1016.state"
    region = "eu-west-1"
  }
}
