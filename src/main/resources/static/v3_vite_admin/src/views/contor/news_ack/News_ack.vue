
<template>
    <el-table :data="tableData" stripe style="width: 100% ; height: 820px;">
        <el-table-column prop="no" label="序号" width="100" />
        <el-table-column prop="title" label="舆情标题" width="300" />
        <el-table-column prop="time" label="上传时间"  width="180" />
        <el-table-column prop="name" label="用户名"  width="100" />
        <el-table-column label="评估结果" width="200" >
            <el-button @click="handleButtonClick()">通过</el-button>
            <el-button @click="handleButtonClick()">不通过</el-button>
        </el-table-column>
        <el-table-column prop="date" label="查看详情" width="200" >
            <el-button @click="handleButtonClick()">查看详情</el-button>
        </el-table-column>
    </el-table>
</template>

<script>
export default {
    data() {
        return {
            tableData :[
                {
                    no: '1',
                    title: '王思聪打人赔款200万？',
                    time:'2022.5.16',
                    name: '张三',
                },
                {
                    no: '2',
                    title: 'Tom',
                    time:'2022.5.16',
                    name: '张三',
                },
                {
                    no: '3',
                    title: 'Tom',
                    time:'2022.5.16',
                    name: '张三',
                },
                {
                    no: '4',
                    title: 'Tom',
                    time:'2022.5.16',
                    name: '张三',
                },
            ]
        };
    },
    methods: {
        handleButtonClick(row) {
            // 处理按钮点击事件
            console.log('点击了编辑按钮，当前行数据：', row);
        }
    }
};
</script>

<style lang="scss" scoped>
.middle {
    display: flex;
    align-items: center;
    justify-content: space-between;

}

</style>