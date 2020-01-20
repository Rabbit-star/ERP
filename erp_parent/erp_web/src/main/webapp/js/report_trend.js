$(function(){
	//加载表格数据
	$('#grid').datagrid({
		queryParams:{},
		columns:[[
		    {field:'name',title:'月份',width:100},
		    {field:'y',title:'销售额',width:100}
		]],
		singleSelect: true,
		onLoadSuccess:function(data){
			showChart1();
			showChart2();
		}
	});

	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formdata = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formdata);
		$('#grid').datagrid({
			url:'report_trendReport',
			queryParams:formdata
		});
	});
	
	
});
function showChart1(){
	var months = new Array();
	for(var i = 1; i <= 12; i++){
		months.push(i + "月");
	}
	//chart();
	$('#trendChart1').highcharts({
		
        title: {
            text:$('#year').combobox('getValue') + "年销售趋势分析",
            x: -20 //center
        },
        subtitle: {
            text: 'Source: www.cxy.com',
            x: -20
        },
        xAxis: {
            categories: months
        },
        yAxis: {
            title: {
                text: '销售额'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            valueSuffix: '元'
        },
        legend: {
            layout: 'vertical',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0
        },
        series: [{
            name: '销售趋势',
            data: $('#grid').datagrid('getRows')
        }]
    });
}

function showChart2(){
	var months = new Array();
	for(var i = 1; i <= 12; i++){
		months.push(i + "月");
	}
	$('#trendChart2').highcharts({
		 chart: {
	            type: 'column'
	        },
		title: {
			text:$('#year').combobox('getValue') + "年销售趋势分析(柱状图)",
			x: -20 //center
		},
		subtitle: {
			text: 'Source: www.itcast.com',
			x: -20
		},
		xAxis: {
			categories: months
		},
		yAxis: {
			title: {
				text: '销售额'
			},
			plotLines: [{
				value: 0,
				width: 1,
				color: '#808080'
			}]
		},
		tooltip: {
			valueSuffix: '元'
		},
		legend: {
			layout: 'vertical',
			align: 'center',
			verticalAlign: 'bottom',
			borderWidth: 0
		},
		series: [{
			name: '销售趋势',
			data: $('#grid').datagrid('getRows'),
			dataLabels:{
	            enabled: true,
	            rotation: -90,
	            color: '#FFFFFF',
	            align: 'right',
	            format: '{point.y:.1f}', // one decimal
	            y: 10, // 10 pixels down from the top
	            style: {
	                fontSize: '13px',
	                fontFamily: 'Verdana, sans-serif'
	            }
	        }
		}],
		 
	});
}