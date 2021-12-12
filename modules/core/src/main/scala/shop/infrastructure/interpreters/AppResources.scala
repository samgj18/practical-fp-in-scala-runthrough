package shop.infrastructure.interpreters

// With sealed class we don't have the copy method for the class
// With the sealed abstract class we need to add the val keyword to make value accessible from the outside
sealed abstract class AppResources {}
