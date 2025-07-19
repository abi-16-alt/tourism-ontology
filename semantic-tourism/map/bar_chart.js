// Simple bar chart using Chart.js
const ctx = document.getElementById('barChart').getContext('2d');
const barChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: ['Agra', 'Delhi', 'Jaipur', 'Mumbai'],
    datasets: [{
      label: 'Average Ratings',
      data: [4.5, 4.2, 4.3, 4.6],
      backgroundColor: 'steelblue'
    }]
  },
  options: {
    responsive: true
  }
});
