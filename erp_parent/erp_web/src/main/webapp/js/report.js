var text = "";
var url = "";
var columns = null;
$(function(){
	if(Request['oper'] == 'sell'){
		url = "report_orderReport";
		$($('#cc').layout('panel','center')).panel('setTitle','销售统计表');
		$($('#cc').layout('panel','east')).panel('setTitle','销售统计图');
		text = '销售统计';
		columns = [[
				    {field:'name',title:'商品类型',width:100},
				    {field:'y',title:'销售额',width:100}
				]];
		/*document.title="我的销售订单";
		btnText = "销售订单录入";*/
		//显示客户
		//$('#addOrdersSupplier').html('客户');
	}
	if(Request['oper'] == 'salesReturn'){
		$($('#cc').layout('panel','center')).panel('setTitle','销售退货统计表');
		$($('#cc').layout('panel','east')).panel('setTitle','销售退货统计图');
		url = "report_returnReport";
		text = '销售退货统计';
		columns = [[
		            {field:'name',title:'商品类型',width:100},
		            {field:'y',title:'退货数量',width:100}
		    ]];
	}
	//加载表格数据
	$('#grid').datagrid({
		url:url,
		columns:columns,
		singleSelect: true,
		onLoadSuccess:function(data){
			//显示图
			showChart(data.rows);
		}
	});

	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formdata = $('#searchForm').serializeJSON();
		if(formdata.endDate != ''){
			formdata.endDate += " 23:59:59";
		}
		$('#grid').datagrid('load',formdata);
	});
	
	
});

//显示图
function showChart(_data){
	$('#pieChart').highcharts({
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text:text
        },
        //信用
        credits:{enabled:false},
        //导出
        exporting:{enabled:true},
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true
                },
                showInLegend: true
            }
        },
        series: [{
            name: "比例",
            colorByPoint: true,
            data: _data
        }]
    });
}