<template>
  <div class="musicCard">
    <b class="textMargin hidden-text">{{ props.title }}</b>
    <a class="textMargin hidden-text" :href="props.url"
      @click.prevent="openLink(props.url)"> {{ props.url }}</a>
  </div>
</template>

<script setup lang="ts">
import { discordSdk } from "@/main";

interface PropsType {
  title: string;
  url: string;
}

const props = defineProps<PropsType>();

const openLink = async (url: string) => {
  try{
    discordSdk.commands.openExternalLink({ url });
  } catch (error){
    console.error(error)
  }
}

</script>

<style scoped>
.musicCard {
  border: 1px solid #000000;
  border-radius: 5px;
  box-sizing: border-box;
  background-color: #303034;
  height: 60px;
  margin: 15px;
  display: flex;
  flex-wrap: nowrap;
  flex-direction: column;
  justify-content: space-evenly;
  color: #ffffff;
  box-shadow: 1px 2px 2px #000000;
}

.hidden-text{
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.textMargin {
  margin-left: 10px;
  margin-right: 10px;
}

a:link {
  color: #97c0fb;
  text-decoration: underline;
}

a:visited {
  color: #be85ef;
  text-decoration: underline;
}
</style>
