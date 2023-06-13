# Get the current working directory
getwd()

# Import the data from the csv file
df <- read.csv("data_set.csv", header = TRUE, sep = ",")

# Put each column into their variables
C_ID <- df$C_ID
C_EquipmentSpend <- df$C_EquipmentSpend
C_ApparelSpend <- df$C_ApparelSpend
C_FootwearSpend <- df$C_FootwearSpend
C_Gender <- df$C_Gender
C_Age <- df$C_Age
C_State <- df$C_State
C_Area <- df$C_Area
C_DeviceType <- df$C_DeviceType
C_ShoppingCart <- df$C_ShoppingCart
C_EmailAd <- df$C_EmailAd
C_ShoppingDuration <- df$C_ShoppingDuration
C_TimeOfShopping <- df$C_TimeOfShopping
C_Number_of_Orders <- df$C_Number_of_Orders
C_Reviews <- df$C_Reviews
C_OrderToDelivery <- df$C_OrderToDelivery
C_Payment <- df$C_Payment
App_Discounts <- df$App_Discounts
App_Tenure <- df$App_Tenure
App_Referral <- df$App_Referral