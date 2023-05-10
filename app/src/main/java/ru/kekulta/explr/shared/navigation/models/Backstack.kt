package ru.kekulta.explr.shared.navigation.models

data class Backstack(val stack: List<Transaction> = listOf())

fun Backstack.setStack(stack: List<Transaction>) = copy(stack = stack)

fun Backstack.addToBackStack(transaction: Transaction) = copy(stack = (stack + transaction))

fun Backstack.popBackStack() = copy(stack = stack.dropLast(1))

fun Backstack.dropAfter(index: Int) = copy(stack = stack.subList(0, index + 1))

val Backstack.lastDestination get() = stack.lastOrNull()?.destination