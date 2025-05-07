package com.xtree.base.lottery.vo

data class SettingVo(
    val pub_channel_id: String,
    val pub_channel_token: String,
    val push_server_host: String,
    val push_service_module: PushServiceModule,
    val push_service_status: String,
    val user_channel_id: String
)

data class PushServiceModule(
    val push_issuecode: String,
    val push_issuetime: String,
    val push_notice: String,
    val push_userbalance: String,
    val push_usermessage: String,
    val push_userwonprize: String
)