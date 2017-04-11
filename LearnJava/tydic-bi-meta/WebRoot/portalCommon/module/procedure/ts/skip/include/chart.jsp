<%@ page language="java"  pageEncoding="UTF-8"%>
<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
	<tr>		
		<td width='50%'>
			<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
				<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
				<td nowrap align='left' class='title_ma1'>
				    <span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
				</td>
				</tr>
				<tr>
					<td>
						<div id='chartdiv_line'></div>
					</td>
				</tr>
			</table>
		</td>
		<td width='50%'>
			<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
				<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
					<td nowrap align='left' class='title_ma1'>
					    <span style='font:12px;font-weight:bold;' id='titleInfo2'></span>
					</td>
					<td align='right'>
					<input type='button' id='city' name='city' class='poster_btn1' value='横向对比'  onclick="lookCity(this)" style='width:70px;'/>
					</td>
				</tr>				
				<tr>
					<td colspan='2'>
						<div id='chartdiv_bar'></div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>