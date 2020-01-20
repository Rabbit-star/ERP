var editIndex=-1;//此参用于定义正在编辑行索引 
var pri = new Array();//此数组预防二次编辑,无price问题
$(function(){
	$('#ordersgrid').datagrid({
		columns:[[
  		    {field:'goodsuuid',title:'商品编号',width:100,editor:{
  		    	type:'numberbox',options:{
  		    		disabled:true
  		    	}}},
  		    {field:'goodsname',title:'商品名称',width:100,editor:{
  		    	type:'combobox',options:{
	    		url:'goods_list',
	    		valueField:'name',
	    		textField:'name',
	    		onSelect:function(goods){
	    			//设置关联值
	    			var goodsuuidEditor = getEditor(editIndex,'goodsuuid');
	    			$(goodsuuidEditor.target).val(goods.uuid);
	    			//设置关联值
	    		
	    			var goodspriceEditor = getEditor(editIndex,'price');
	    			if(Request['type']*1==1){
	    				//设置为进货价
	    				$(goodspriceEditor.target).val(goods.inprice);
	    				
	    			}
	    			if(Request['type']*1==2){
	    				//设置为销售价
	    				$(goodspriceEditor.target).val(goods.outprice);
	    				
	    			}
	    			//往price数组里存值
	    			pri[editIndex] = $(goodspriceEditor.target).val();
	    			//聚集焦点
	    			var numEditor = getEditor(editIndex,'num');
	    			$(numEditor.target).select();
	    			//绑定事件
	    			bindGridEditor();
	    			//重新选择,再次计算
	    			cal();
	    			sum();
	    		}
	    		
  		    	} }},
  		    {field:'price',title:'价格',width:100,editor:{type:'numberbox',options:{precision:2,prefix:'￥'}}},
  		    {field:'num',title:'数量',width:100,editor:'numberbox'},
  		    {field:'money',title:'金额',width:100,editor:{type:'numberbox',options:{precision:2,prefix:'￥',disabled:true}}},
  		    {field:'-',title:'操作',width:100,formatter:function(value,row,rowIndex){
	
				if(row.num != '合计'){
					  return '<a href="javascript:void(0)" onclick="deleteRow(' + rowIndex + ')">删除</a>';
				  }
    		}}
		]],
		//设置单行选中
		singleSelect:true,
		toolbar: [{
			text:'新增',
			iconCls: 'icon-add',
			handler: function(){
				//判断,给新行加自定义索引,方便传参
				if(editIndex>-1){
					$('#ordersgrid').datagrid('endEdit',editIndex);
				}
				//设置新增行
				$('#ordersgrid').datagrid('appendRow',{num:0,money:0 });
				var rows = $('#ordersgrid').datagrid('getRows');
				//需要先加上列属性里的编辑器
				editIndex=rows.length-1;
				$('#ordersgrid').datagrid('beginEdit',editIndex);
				var goodsnameEditor = getEditor(editIndex,'goodsname');
				$(goodsnameEditor.target).select();
				
			}
		},'-',{//这里的杠杠是分割两个按钮,页面更加美观
			text:'提交',
			iconCls: 'icon-save',
			handler: function(){
				//清空数组
				pri.splice(0, pri.length);
				//上来就提交,拒绝他
				if(editIndex>-1){
					//存在可编辑的行就关闭
					$('#ordersgrid').datagrid('endEdit',editIndex);
					
				}
				var rows = $('#ordersgrid').datagrid('getRows');
				if(rows.length==0){
					return;
				}
				var formdata = $('#orderForm').serializeJSON();
				//转换成json字符串
				//给formdata加了一个json属性，key。同时再给它赋值
				formdata.json = JSON.stringify(rows);
				$.ajax({
					url:'orders_add?t.type='+Request['type'],
					type: 'post',
					data:formdata,
					dataType:'json',
					success:function(rtn){
						$.messager.alert('提示',rtn.message,'info',function(){
							if(rtn.success){
								//清空供应商
								$('#supplier').combogrid('reset');
								//清空表格
								$('#ordersgrid').datagrid('loadData',{total:0, rows:[],footer:[{num: '合计', money: 0}]});
								//关闭增加订单的窗口
								$('#addOrdersDlg').dialog('close');
								//刷新订单列表
								$('#grid').datagrid("reload");
							}
							
						});
					
					}
					
					
				});
				
				
			}
		}],
		//行号显示
		rownumbers:true,
		onClickRow(rowIndex,rowData){
			$('#ordersgrid').datagrid('endEdit',editIndex);
			editIndex=rowIndex;//把正在编辑行索引,设置到点击位置处
			$('#ordersgrid').datagrid('beginEdit',editIndex);
			var numEditor = getEditor(editIndex,'num');
			$(numEditor.target).select();
			bindGridEditor();
		},
		//显示行脚
		showFooter:true
		
	});
	//加载脚
	$('#ordersgrid').datagrid('reloadFooter',[{num: '合计', money: 0}]);
	
	//加载供应商下拉表格
	$('#supplier').combogrid({
		panelWidth:700,
		idField:'uuid',
		textField:'name',
		url:'supplier_list?t1.type='+Request['type'],
		columns:[[
			{field:'uuid',title:'编号',width:100 },
  		    {field:'name',title:'公司名称',width:100},
  		    {field:'address',title:'联系地址',width:100},
  		    {field:'contact',title:'联系人',width:100},
  		    {field:'tele',title:'联系电话',width:100},
  		    {field:'email',title:'邮件地址',width:200}
			
		]],
		mode:'remote'
	
	
		
		
	});
	
}); 



//此方法返回单元格编辑器
function getEditor(editIndex,field){
	
	return  $('#ordersgrid').datagrid('getEditor',{index:editIndex,field:field});
	
}
//此方法用于计算单行金额
function cal(){
	//取值
	var numEditor = getEditor(editIndex,'num');
	var num = $(numEditor.target).val()*1;
	//取值
	var priceEditor = getEditor(editIndex,'price');
	var price = $(priceEditor.target).val()*1;
		if(isNaN(price)){
			//数组派上用场,再次点击编辑,price为Nan,不利于修改数量
			price = pri[editIndex];
		}
		//计算
	var total = num*price;
	total = total.toFixed(2);
	var moneyEdit=getEditor(editIndex,'money');
	//设置计算金额
	$(moneyEdit.target).val(total);
	//更新表格中的数据(设置row的json对象里的key的值)
	$('#ordersgrid').datagrid('getRows')[editIndex].money=total;
}
//绑定键盘的输入事件
function bindGridEditor(){
	//绑定数量改变事件
	var numEditor = getEditor(editIndex,'num');
	$(numEditor.target).bind('keyup',function(){
		cal();
		sum();
	});
	//绑定价格改变事件
	var priceEditor = getEditor(editIndex,'price');
	$(priceEditor.target).bind('keyup',function(){
		cal();
		 sum();
	});
	
}
//此方法用于计算总计金额
function sum(){
	//alert(pri);
	//获取所有行
	var rows = $('#ordersgrid').datagrid('getRows');
	//设置初始总计金额
	var total = 0;
	$.each(rows,function(i,row){
		total += parseFloat(row.money);
	});
	total = total.toFixed(2);
	//设置合计金额到行脚里
	$('#ordersgrid').datagrid('reloadFooter',[{num:'合计',money:total}]);
	
}
function deleteRow(rowIndex){
	 pri.splice(rowIndex,1);
	
	//alert(JSON.stringify(data));
	//关闭编辑
	$('#ordersgrid').datagrid('endEdit',editIndex);
	//删除行
	$('#ordersgrid').datagrid('deleteRow',rowIndex);
	
	var data = $('#ordersgrid').datagrid('getData');
	//重新加载数据
	$('#ordersgrid').datagrid('loadData',data);
	//计算合计
	sum();
}


