$(function(){
	//列表
	$('#grid').datagrid({
		url:'storedetail_listByPage',
		columns:[[
		  		    {field:'uuid',title:'编号',width:100},
		  		    {field:'storeName',title:'仓库',width:100},
		  		    {field:'goodsName',title:'商品',width:100},
		  		    {field:'num',title:'数量',width:100}
					]],
		singleSelect: true,
		pagination: true
	});
	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formData);
	});
	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formData);
	});
	$('#grid').datagrid({
		onDblClickRow:function(rowIndex, rowData){
			$('#goodsname').html(rowData.goodsName);
			$('#storename').html(rowData.storeName);
			$('#itemgrid').datagrid({
				url:'storeoper_listByPage?t1.storeuuid='+rowData.storeuuid+'&t1.goodsuuid='+rowData.goodsuuid,
				columns:[[
					{field:'uuid',title:'编号',width:48},
					{field:'empName',title:'员工',width:100},
					{field:'opertime',title:'操作日期',width:200,formatter:function(value){
		  		    	return new Date(value).Format("yyyy-MM-dd hh:mm:ss");}},
					{field:'type',title:'类型',width:98,formatter:function(value){
						if(value == 1){return '入库';}
						if(value == 2){return '出库';}
						
					}}
					
				]],
				singleSelect:true,
				pagination:true,
				
			
			});
			
			
			//打开窗口
			$('#storeoperDlg').dialog('open');
		
		}
	});
	
})
	
