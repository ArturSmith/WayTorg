package com.way_torg.myapplication.extensions

import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.StackAnimator

fun StackAnimator.invert(): StackAnimator =
    StackAnimator { direction, isInitial, onFinished, content ->
        this(
            direction = direction.invert(),
            isInitial = isInitial,
            onFinished = onFinished,
            content = content,
        )
    }

private fun Direction.invert(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }