#creating bar charts, pie charts and box charts
data <- read.csv("weekly_sales_flavours.csv")
data
nrows <- nrow(data)
ncols <- ncol(data)
nrows
ncols
# Extract the data for staff
staff <- subset(data, Customers=="Staff")
staff
nrowsStaff <- nrow(staff)
ncolsStaff <- ncol(staff)
nrowsStaff
ncolsStaff
#Aggregating data per flavour for staff
sum_staff <- colSums(staff[4:ncolsStaff])
sum_staff
#bar charts
barplot(sum_staff, las = 2, col = "lightblue")
title(main = "Staff")

#changing margin space of the barcharts
barplot(sum_staff, las = 2, col = "lightblue")
title(main = "Staff")
par(mar = c(8, 4, 4, 4), cex = 0.7)

#Pie charts
par(cex=0.5)
pie(sum_staff)
title(main = "Staff")

#Boxplots
par(mar = c(8, 4, 4, 1), cex = 0.65)
boxplot(staff[, 4:ncols], las = 3, col = "lightblue");
title(main = "Staff", ylab="ice-cream sales",xlab="week")

