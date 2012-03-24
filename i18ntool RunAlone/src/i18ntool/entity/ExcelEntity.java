/**
 * 
 * @author fernado
 * @date Jan 7, 2011
 */
package i18ntool.entity;


import iceworld.fernado.consts.Constants;

import java.io.Serializable;
import java.util.RandomAccess;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
 * 
 * Map<key-name, values>
 * Map<key-name, Map<language, ExcelEntity>>
 * @author changping.shui
 *
 */

public class ExcelEntity {

	private String value;
	
	private HSSFCellStyle style;

	public String getValue() {
		return value;
	}

	public void setValue(final String value) {
		this.value = value;
	}
	
	public HSSFCellStyle getStyle() {
		return style;
	}

	public void setStyle(final HSSFCellStyle style) {
		this.style = style;
	}
	
	public boolean isStyleEmpty() {
		return null == style;
	}
	
	private static final ExcelEntity EMPTY_EXCELENTITY = new EmptyExcelEnity();
	
	public static final ExcelEntity emptyExcelEntity() {
		return EMPTY_EXCELENTITY;
	}
	
	private static class EmptyExcelEnity extends ExcelEntity implements RandomAccess, Serializable {
		private static final long serialVersionUID = 1L;

		public String getValue() {
			return Constants.EMPTY_STRING;
		}
		
		public HSSFCellStyle getStyle() {
			throw new NullPointerException();
		}
		
		private Object readResolve() {
			return EMPTY_EXCELENTITY;
		}
	}
}
