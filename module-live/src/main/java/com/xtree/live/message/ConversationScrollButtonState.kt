package com.xtree.live.message

data class ConversationScrollButtonState(
    val showScrollButtonsForScrollPosition: Boolean = false,
    val willScrollToBottomOnNewMessage: Boolean = true,
    val unreadCount: Int = 0
) {
    val showScrollButtons: Boolean
        get() = showScrollButtonsForScrollPosition || (!willScrollToBottomOnNewMessage && unreadCount > 0)

    override fun toString(): String {
        return "showScrollButtonsForScrollPosition:$showScrollButtonsForScrollPosition,willScrollToBottomOnNewMessage:$willScrollToBottomOnNewMessage,unreadCount:$unreadCount"
    }
}