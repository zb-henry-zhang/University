#preworkshop 2
#reading csv files
library(readr)
data <- read.csv("weekly_sales_staff_students.csv")
data
#extracting sales for each customer group
sales_staff <- data$staff
sales_staff
#plotting graph of the sales staff
plot(sales_staff)
#line graph
plot(sales_staff, type = "l")
#exploring different options such as colour and line type (lty=1 for normal line and lty =2 for dashed line)
plot(sales_staff,type = "l", lty=2, col = "red")
#plotting multiple lines
plot(sales_staff,type = "l", lty=2, col = "red")
lines(data$student, col = "steelblue4")
#maximum sales from students
max(data$student)
#maximum sales for students
max(data$staff)
#plotting sales for students
plot(sales_staff,type = "l", lty=2, col = "red", ylim = c(0,13000))
lines(data$student, col = "steelblue4")
#plotting both lines
plot(sales_staff,type = "l", lty=2, col = "red", ylim = c(0,13000))
lines(data$student, col = "steelblue4")
legend(18, 13000, c("Staff", "Students"), col = c("red", "steelblue4"), lty = c(2,1))
# labelling
plot(sales_staff,type = "l", lty=2, col = "red", ylim = c(0,13000), ann = FALSE)
lines(data$student, col = "steelblue4")
legend(18, 13000, c("Staff", "Students"), col = c("red", "steelblue4"), lty = c(2,1))
title(xlab = "Weeks", ylab = "Sales", main = "Aggregate Sales")

