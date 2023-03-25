<template>
    <el-card shadow="never" class="index">
        <template #header>
            <div class="card_header">
                <b>火眼金睛</b>
            </div>
        </template>
        <!-- <h1>高德地图</h1> -->
        <br />
        
        <!-- <Amap /> -->
        <Eye />
    </el-card>
</template>

<script>
import Amap from "../components/Amap.vue";
import Eye from "../components/Eye.vue";

export default {
    components: { Amap , Eye},
    setup() {
        return {};
    }
};
</script>

<style lang="scss" scoped>
.card_header {
    display: flex;
    align-items: center;
    justify-content: space-between;
}
</style>
