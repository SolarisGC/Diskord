package com.jessecorbett.diskord.util

import com.jessecorbett.diskord.api.common.*
import com.jessecorbett.diskord.api.channel.Embed
import com.jessecorbett.diskord.api.channel.ChannelClient
import com.jessecorbett.diskord.api.channel.CreateMessage
import com.jessecorbett.diskord.api.channel.FileData
import com.jessecorbett.diskord.api.guild.GuildClient
import com.jessecorbett.diskord.api.guild.PatchGuildMember
import com.jessecorbett.diskord.api.guild.PatchGuildMemberNickname

/*
 * Primitive extensions
 */

/**
 * Shortcut to add a spoiler to text.
 *
 * @return This string wrapped with markdown formatting.
 */
public fun String.withSpoiler(): String = "||$this||"

/**
 * Shortcut to add a italics to text.
 *
 * @return This string wrapped with markdown formatting.
 */
public fun String.withItalics(): String = "*$this*"

/**
 * Shortcut to bold text.
 *
 * @return This string wrapped with markdown formatting.
 */
public fun String.withBold(): String = "**$this**"

/**
 * Shortcut to add an underline to text.
 *
 * @return This string wrapped with markdown formatting.
 */
public fun String.withUnderline(): String = "__${this}__"


/**
 * Shortcut to add a strikethrough to text.
 *
 * @return This string wrapped with markdown formatting.
 */
public fun String.withStrikethrough(): String = "~~$this~~"

/**
 * Shortcut to put the code in a single line code block.
 *
 * @return This string wrapped with markdown formatting.
 */
public fun String.withSingleLineCode(): String = "`$this`"

/**
 * Shortcut to put the code in a multi line code block.
 *
 * @return This string wrapped with markdown formatting.
 */
public fun String.withMultiLineCode(): String = "```$this```"

/**
 * Shortcut to put the code in a multi line code block with a language style.
 *
 * @param language The language to add style for.
 *
 * @return This string wrapped with markdown formatting.
 */
public fun String.withMultiLineCode(language: String): String = "```$language $this```"

/*
 * Message extensions
 */

/**
 * Shortcut to check if a message is from a user.
 */
public val Message.isFromUser: Boolean
    get() = !(author.isBot ?: false) && webhookId == null

/**
 * Shortcut to check if a message is from a bot.
 */
public val Message.isFromBot: Boolean
    get() = (author.isBot ?: false) && this.webhookId == null

/**
 * Shortcut to check if a message is from a webhook.
 */
public val Message.isFromWebhook: Boolean
    get() = webhookId != null


/**
 * Shortcut to get the [User.id] of the [Message.author].
 */
public val Message.authorId: String
    get() = author.id

/**
 * Shortcut to get the [Message.content] split into words.
 */
public val Message.words: List<String>
    get() = content.split(" ")


/*
 * User extensions
 */

/**
 * Convenience method to turn a user id into a formatted mention for chat.
 *
 * @return the user in chat mention format.
 */
public fun String.toUserMention(): String = "<@$this>"

/**
 * Convenience method to turn a user into a formatted mention for chat.
 *
 * @return the user in chat mention format.
 */
public val User.mention: String
    get() = id.toUserMention()

/**
 * Convenience method to check if a user has a custom avatar.
 */
public val User.hasCustomAvatar: Boolean
    get() = avatarHash != null


/*
 * Role extensions
 */
/**
 * Convenience method to turn a role id into a formatted mention for chat.
 *
 * @return the role in chat mention format.
 */
public fun String.toRoleMention(): String = "<@&$this>"

/**
 * Convenience method to turn a role into a formatted mention for chat.
 *
 * @return the role in chat mention format.
 */
public val Role.mention: String
    get() = id.toRoleMention()


/*
 * Channel extensions
 */
/**
 * Convenience method to turn a channel into a formatted mention for chat.
 *
 * @return the channel in chat mention format.
 */
public val GuildText.mention: String
    get() = "#$name"


/*
 * Emoji extensions
 */

/**
 * Convenience method to check if an emoji is a Unicode emoji.
 */
public val Emoji.isUnicode: Boolean
    get() = id == null

/**
 * Convenience method to check if an emoji is a custom emoji.
 */
public val Emoji.isCustom: Boolean
    get() = !isUnicode

/**
 * Convenience method to convert an emoji object into a chat formatted emoji.
 *
 * @return the emoji in chat format.
 */
public val Emoji.tag: String
    get() = if (isUnicode) name!! else "<${if (isAnimated) "a" else ""}:$name:$id>"


/*
 * Client extensions
 */

/**
 * Calls [ChannelClient.createMessage] for text messages without needing to create a [CreateMessage] object first.
 *
 * @param message The text message to send.
 * @param embed The embed to include with the message.
 *
 * @return the created [Message].
 * @throws com.jessecorbett.diskord.api.exceptions.DiscordException upon client errors.
 */
public suspend fun ChannelClient.sendMessage(message: String = "", embed: Embed? = null): Message {
    return createMessage(CreateMessage(content = message, embed = embed))
}

/**
 * Calls [ChannelClient.createMessage] for text messages without needing to create a [CreateMessage] object first.
 *
 * @param data The file to attach.
 * @param comment The comment to send with the file.
 *
 * @return the created [Message].
 * @throws com.jessecorbett.diskord.api.exceptions.DiscordException upon client errors.
 */
public suspend fun ChannelClient.sendFile(data: FileData, comment: String = ""): Message {
    return createMessage(CreateMessage(content = comment), data)
}

public suspend fun ChannelClient.addMessageReaction(messageId: String, emojiId: String, emojiName: String) {
    addMessageReaction(messageId, Emoji(emojiId, emojiName))
}

/**
 * Changes the user's nickname in this client's guild.
 *
 * @param nickname the new nickname.
 *
 * @return the created [Message].
 * @throws com.jessecorbett.diskord.api.exceptions.DiscordException upon client errors.
 */
public suspend fun GuildClient.changeNickname(nickname: String) {
    changeMemberNickname(PatchGuildMemberNickname(nickname))
}

/**
 * Changes a user's nickname in this client's guild.
 *
 * @param userId the id of the user whose nickname to change.
 * @param nickname the new nickname.
 *
 * @throws com.jessecorbett.diskord.api.exceptions.DiscordException upon client errors.
 */
public suspend fun GuildClient.changeNickname(userId: String, nickname: String) {
    updateMember(userId, PatchGuildMember(nickname))
}
