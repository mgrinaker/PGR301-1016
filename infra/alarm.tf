resource "aws_cloudwatch_metric_alarm" "zerosum" {
  alarm_name                = "total-amount-over-five-minutes"
  namespace                 = "1016"
  metric_name               = "carts.count"

  comparison_operator       = "GreaterThanThreshold"
  threshold                 = "5"
  evaluation_periods        = "5"
  period                    = "300"

  statistic                 = "Maximum"

  alarm_description         = "This alarm goes off as soon as the total amount of carts exceeded if over three repeated periods, on five minutes, goes over five minutes"
  insufficient_data_actions = []
  alarm_actions       = [aws_sns_topic.user_updates.arn]
}

resource "aws_sns_topic" "user_updates" {
  name = var.candidate_id
}

resource "aws_sns_topic_subscription" "user_updates_sqs_target" {
  topic_arn = aws_sns_topic.user_updates.arn
  protocol  = "email"
  endpoint  = var.candidate_email
}