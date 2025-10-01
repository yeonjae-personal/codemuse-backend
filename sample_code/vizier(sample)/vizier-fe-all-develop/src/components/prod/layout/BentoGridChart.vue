<template>
    <div>
        <div>
            <h2>Item Volume</h2>
            <p>Inquire all items managed in the solution by offers type and item type.</p>
        </div>
        <div class="chart-container">
            <div>
                <h3>Offers</h3>
                <DoughnutChart :chart-data="offersData" />
            </div>
            <div>
                <h3>Items</h3>
                <DoughnutChart :chart-data="itemsData" />
            </div>
        </div>
    </div>
</template>

<script>
import { Doughnut } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale);

export default {
    components: {
        DoughnutChart: {
            extends: Doughnut,
            props: ['chartData'],
            mounted() {
                this.renderChart(this.chartData, { responsive: true, maintainAspectRatio: false });
            },
        },
    },
    data() {
        return {
            offersData: {
                labels: ['Banner', 'Discount', 'Add-On', 'Package'],
                datasets: [
                    {
                        data: [43, 12, 29, 16],
                        backgroundColor: ['#4bc0c0', '#ffcd56', '#36a2eb', '#9966ff'],
                    },
                ],
            },
            itemsData: {
                labels: ['Company', 'Package', 'Product', 'Discount'],
                datasets: [
                    {
                        data: [24, 16, 30, 18],
                        backgroundColor: ['#ff6384', '#36a2eb', '#ffcd56', '#ff9f40'],
                    },
                ],
            },
        };
    },
};
</script>

<style scoped>
.chart-container {
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-top: 20px;
}

.chart-container>div {
    width: 300px;
    height: 300px;
}
</style>