/**
 * 盘盈盘亏
 */
$(function(){
	
	var url = 'inventory_listByPage';
	
	//如果盘盈盘亏登记
	if(Request['oper'] == 'inventory'){
		url == url;
		document.title="盘盈盘亏登记";
	}
	
	//如果审核业务，加上state=0，只查询出未审核的订单
	if(Request['oper'] == 'doCheck'){
		url += "?t1.state=0";
		document.title="盘盈盘亏审核";
	}

	if(Request['oper'] == 'doCheck'){
		$('#grid').datagrid({
			url:url,
			columns:[[
			  		    {field:'uuid',title:'编号',width:100},
			  		    {field:'goodname',title:'商品',width:100},
			  		    {field:'storename',title:'仓库',width:100},
			  		    {field:'num',title:'数量',width:100},
			  		    {field:'type',title:'类型',width:100,formatter:getType},
			  		    {field:'createtime',title:'登记日期',width:100,formatter:formatDate},
			  		    {field:'checktime',title:'审核日期',width:100,formatter:formatDate},
			  		    {field:'creatername',title:'登记人',width:100},
			  		    {field:'checkername',title:'审核人',width:100},
			  		    {field:'state',title:'状态',width:100,formatter:getState}
						]],
						
				singleSelect:true,
				pagination:true,
				fitColumns:true,
			onDblClickRow : function(rowIndex, rowData) {
				// rowIndex， 行的索引
				// rowData， 行里的数据
				// alert(JSON.stringify(rowData));
				// 显示详情
				$('#uuid').html(rowData.uuid);
				$('#createtime').html(formatDate(rowData.createtime));
				$('#goodsuuid').html(rowData.goodname);
				$('#storeuuid').html(rowData.storename);
				$('#num').html(rowData.num);
				$('#type').html(getType(rowData.type));
				$('#remark').html(rowData.remark);
				if(Request['oper'] == 'doCheck'){
					//打开审核窗口
					$('#examineDlg').dialog('open');
				}
			}
		});
		// 点击查询按钮
		$('#btnSearch').bind('click', function() {
			// 把表单数据转换成json对象
			var formData = $('#searchForm').serializeJSON();
			// alert(formData);
			$('#grid').datagrid('load', formData);
		});
	}
	
	if(Request['oper'] == 'inventory'){
		$('#addgrid').datagrid({
			url:url,
			columns:[[
			  		    {field:'uuid',title:'编号',width:100},
			  		    {field:'goodname',title:'商品',width:100},
			  		    {field:'storename',title:'仓库',width:100},
			  		    {field:'num',title:'数量',width:100},
			  		    {field:'type',title:'类型',width:100,formatter:getType},
			  		    {field:'createtime',title:'登记日期',width:100,formatter:formatDate},
			  		    {field:'checktime',title:'审核日期',width:100,formatter:formatDate},
			  		    {field:'creatername',title:'登记人',width:100},
			  		    {field:'checkername',title:'审核人',width:100},
			  		    {field:'state',title:'状态',width:100,formatter:getState}
						]],
						toolbar: [
							  		{
							  			text: '盘盈盘亏登记',
							  			iconCls: 'icon-add',
							  			handler: function(){
							  				//设置保存按钮提交的方法为add
							  				method = "add";
							  				//关闭编辑窗口
							  				$('#editDlg').dialog('open');
							  			}
							  		}],		
				singleSelect:true,
				pagination:true,
				fitColumns:true,
		});
		// 点击查询按钮
		$('#btnSearch').bind('click', function() {
			// 把表单数据转换成json对象
			var formData = $('#searchForm').serializeJSON();
			// alert(formData);
			$('#addgrid').datagrid('load', formData);
		});
	}
	


	//初始化编辑窗口
	$('#editDlg').window({
		title: '编辑',//窗口标题
		width: 225,//窗口宽度
		height: 230,//窗口高度
		closed: true,//窗口是是否为关闭状态, true：表示关闭
		modal: true//模式窗口
	});

	$('#examineDlg').dialog({
		title : '盘盈盘亏审核', // 窗口标题
		width : 250, // 窗口宽度
		height : 275, // 窗口高度
		closed : true, // 窗口是是否为关闭状态, true：表示关闭
		modal : true,
		buttons : [ {
			text : '审核',
			iconCls : 'icon-add',
			handler : doCheck
		} ]
	});

	// 点击保存按钮
	$('#btnSave').bind('click', function() {

		var isvalidate = $('#editForm').form('validate');
		if (isvalidate == false) {
			return;
		}
		var formData = $('#editForm').serializeJSON();
		$.ajax({
			url : 'inventory_add',
			data : formData,
			dataType : 'json',
			type : 'post',
			success : function(rtn) {
				$.messager.alert("提示", rtn.message, 'info', function() {
					// 成功的话，我们要关闭窗口
					$('#editDlg').window('close');
					$('#editForm').form('clear');
					// 刷新表格数据
					$('#addgrid').datagrid('reload');
					/*$('#grid').datagrid('reload');*/
				});
			}
		});
	});
})

function formatDate(value) {
	if (value != null) {
		return new Date(value).Format('yyyy-MM-dd');
	} else {
		return "";
	}
}

/**
 * 获取类型
 * 
 * @returns 1:盘盈, 2:盘亏
 */
function getType(value) {

	switch (value * 1) {
	case 1:
		return '盘盈';
	case 2:
		return '盘亏';

	default:
		return '';
	}
}

/**
 * 获取状态
 * 
 * @returns 0:未审核, 1:审核
 */
function getState(value) {

	switch (value * 1) {
	case 0:
		return '未审核';
	case 1:
		return '已审核';

	default:
		return '';
	}
}

/**
 * 审核
 */
function doCheck() {
	$.messager.confirm('确认', '确认要审核吗？', function(yes) {
		if (yes) {
			var formData = $('#examineForm').serializeJSON();
			$.ajax({
				url : 'inventory_doCheck?id=' + $('#uuid').html(),
				dataType : 'json',
				type : 'post',
				data : formData,
				success : function(rtn) {
					$.messager.alert('提示', rtn.message, 'info', function() {
						if (rtn.success) {
							// 关闭窗口
							$('#examineDlg').dialog('close');
							// 刷新表格
							$('#grid').datagrid('reload');
						}
					});
				}
			});
		}
	});
}