data <- read.csv("weekly_sales_flavours.csv")

student <- subset(data, Customers=="Student")

student_fruit <- subset(student,select = c(Apricot,Banana,Cherry.Almond,Ginger,Lime.Coconut,Mango,Pure.Coconut,Red.Bean))
student_caramel <- subset(student,select = S.Caramel)
student_chocolate <- subset(student,select = c(Chocolate,Mint.Choco))
student_tea_coffee <- subset(student,select = c(Chai.Tea,Green.Tea,Coffee))
student_nut <- subset(student,select = c(Hazelnut,Pistachio))

student_fruit_sum <- sum(student_fruit)
student_caramel_sum <- sum(student_caramel)
student_chocolate_sum <- sum(student_chocolate)
student_tea_coffee_sum <- sum(student_tea_coffee)
student_nut_sum <- sum(student_nut)

student_flavour_sales <- c(student_fruit_sum, student_caramel_sum, student_chocolate_sum, student_tea_coffee_sum, student_nut_sum)
col_names <- c("Fruit", "Caramel", "Chocolate", "Tea/coffee", "Nut")

barplot(student_flavour_sales, las = 2, col = "lightblue", ylim = c(0,50000), names.arg = col_names)
title(main = "Student Sales - Flavour Groups")

# Calculate the column sums
column_sums <- colSums(data)

# Print the column sums
print(column_sums)

help("colSums")

data <- read.csv("weekly_sales_flavours.csv")

# Convert the data to numeric
data[, 3:ncol(data)] <- sapply(data[, 3:ncol(data)], as.numeric)

# Sum up the sales for each flavor
flavor_sums <- colSums(data[, 3:ncol(data)], na.rm = TRUE)
barplot(flavor_sums, main = "Total Sales for Each Flavor", xlab = "Flavor", ylab = "Total Sales", col = "blue")
