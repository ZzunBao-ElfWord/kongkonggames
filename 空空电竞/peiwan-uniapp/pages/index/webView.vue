<template>
    <web-view v-if="url" :src="url"></web-view>
</template>
 
<script>
    import { isAiStoreUrl, ensureAiStoreLogin } from '@/utils/ai-store-access'
    export default {
        data() {
            return {
                url: null  //要打开的外部链接
            }
        },
        onLoad: async function (option) {
            const target = option && option.url
            if (!target) return
            // 防止绕过首页，直接打开内置网页路由进入 AI Store。
            if (isAiStoreUrl(target) && !await ensureAiStoreLogin(this.$Request, true)) return
            this.url = target
        }
    }
</script>
