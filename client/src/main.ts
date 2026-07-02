import { createApp, computed } from 'vue'
import { createPinia } from 'pinia'
import App from '@/App.vue'

import { DiscordSDK } from "@discord/embedded-app-sdk";

export const discordSdk = new DiscordSDK(import.meta.env.VITE_DISCORD_CLIENT_ID);

export const channelId = computed(() => discordSdk.channelId)

export const guildId = computed(() => discordSdk.guildId)

const app = createApp(App)

app.use(createPinia())

app.mount('#app')
