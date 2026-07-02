<template>
  <svg
    version="1.1"
    id="_x32_"
    xmlns="http://www.w3.org/2000/svg"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    x="0px"
    y="0px"
    viewBox="190 168 155 174"
    style="opacity: 1"
    xml:space="preserve"
    :width="props.width"
    :height="props.height"
  >
    <g>
      <path
        d="M351.062,258.898l-144,85.945c-1.031,0.626-2.344,0.657-3.406,0.031
        c-1.031-0.594-1.687-1.702-1.687-2.937v-85.946v-85.946c0-1.218,0.656-2.343,1.687-2.938c1.062-0.609,2.375-0.578,3.406,0.031
        l144,85.962c1.031,0.586,1.641,1.718,1.641,2.89C352.703,257.187,352.094,258.297,351.062,258.898z"
        style="fill: rgb(255, 255, 255)"
      />
    </g>
  </svg>
</template>

<script setup lang="ts">
interface PropsType {
  height: string;
  width: string;
}

const props = defineProps<PropsType>();
</script>
