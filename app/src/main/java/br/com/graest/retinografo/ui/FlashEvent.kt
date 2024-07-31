package br.com.graest.retinografo.ui

sealed interface FlashEvent {
    data class OnSetFlash(val flashOn: Boolean): FlashEvent
}