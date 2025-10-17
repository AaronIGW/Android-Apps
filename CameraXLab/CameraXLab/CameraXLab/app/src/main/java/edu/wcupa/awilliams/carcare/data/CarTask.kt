package edu.wcupa.awilliams.carcare.data

data class CarTask(
    val title: String,
    val note: String
)

object DefaultTasks {
    val list = listOf(
        CarTask("Oil Change", "Every ~3 months or 3,000–5,000 miles"),
        CarTask("Tire Rotation", "Every ~6 months or 6,000–8,000 miles"),
        CarTask("Brake Inspection", "Check pads and fluid"),
        CarTask("Wiper Replacement", "Before rainy/snow season"),
        CarTask("Fluid Top-Off", "Coolant, washer, brake, power steering"),
        CarTask("Air Filter", "Engine/cabin as needed"),
        CarTask("Battery Check", "Test before winter/summer"),
        CarTask("State Inspection", "Annual as required")
    )
}
