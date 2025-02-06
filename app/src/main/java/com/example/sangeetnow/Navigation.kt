package com.example.sangeetnow
interface Destinations{
    val route:String
}
object WelcomePage:Destinations{
    override val route: String="WelcomePage"
}
object Search:Destinations{
    override val route: String="Search"
}
object Title:Destinations{
    override val route: String="Title"
}
object AccountPage:Destinations{
    override val route: String="AccountPage"
}
object LoginPage:Destinations{
    override val route: String="LoginPage"
}
