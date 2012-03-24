/**
 * 
 * @author fernado
 * @date Jan 7, 2011
 */
package i18ntool.file.impl;

import i18ntool.consts.Constant;
import i18ntool.consts.Status;
import i18ntool.entity.TreeNode;
import i18ntool.entity.ValueEntity;
import i18ntool.util.LogManager;
import i18ntool.util.MessageAssistant;
import i18ntool.util.NodeAssistant;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;
import iceworld.fernado.file.ILoadResource;
import iceworld.fernado.util.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;


public class LoadExcel implements ILoadResource {

	private static final String ID = LoadExcel.class.getName();
	private static final Logger log = LogManager.getLogger(ID);
	
	private String i18nFileFolder;
	
	public  LoadExcel(final String i18nFileFolder) {
		this.i18nFileFolder = i18nFileFolder;
	}
	
	@Override
	public boolean readResources() {
		NodeAssistant.getInstance().clearRemovedNodes();
		String filePath = MessageAssistant.getInstance().getFolder() + i18nFileFolder;
		String file = Utils.getFileWithSuffix(filePath, Constant.EXCEL_SUFFIX);
		if (Utils.isEmpty(file)) {
			return true;
		}

		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(Display
				.getCurrent().getActiveShell());
		try {
			progressMonitorDialog.run(false, true, new LoadProgress(filePath));
		} catch (InvocationTargetException e) {
			log.log(Level.WARNING, "Save Properties to File -- Invocation Target");
		} catch (InterruptedException e) {
			log.log(Level.WARNING, "Save Properties to File -- Interrupted");
		}
		
		return false;
	}
	
	private class LoadProgress implements IRunnableWithProgress {
		private INode root;
		private String filePath;
		private int work = 10000;
		private int iCurWork = 0;
		/**
		 * <key, <language, value>>
		 */
		private final Map<String, Map<String, String>> excelMap = new TreeMap<String, Map<String, String>>();
		private final Map<String, String> commentMap = new HashMap<String, String>();
		
		public LoadProgress(final String filePath) {
			this.filePath = filePath;
		}
		
		@Override
		public void run(final IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
			root = NodeAssistant.getInstance().getData();
			Display.findDisplay(Thread.currentThread()).readAndDispatch();
			doReadExcel(monitor);
		}
		
		private void doReadExcel(final IProgressMonitor monitor) {
			monitor.beginTask("Loading Excel", work);
			POIFSFileSystem fs = null;
			HSSFWorkbook wb = null;
			InputStream is = null;
			
			try {
				is = new FileInputStream(filePath);
				fs = new POIFSFileSystem(is);
				wb = new  HSSFWorkbook(fs);
				HSSFSheet sheet = wb.getSheetAt(0);
				HSSFRow row = sheet.getRow(0);
				Map<String, Integer> messageFileMap = new LinkedHashMap<String, Integer>();
				createMessageMapToColumn(row, messageFileMap);
				if (!monitor.isCanceled()) {
					iCurWork = 10;
					monitor.worked(iCurWork);
				}
				createMessageValue(sheet, messageFileMap, monitor);
				createTreeNodes(monitor);
			} catch (FileNotFoundException e) {
				log.log(Level.WARNING, "CANT FIND THE FILE OF " + filePath);
			} catch (IOException e) {
				log.log(Level.WARNING, "CANT READ THE FILE OF " + filePath);
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					log.log(Level.WARNING, "CANT CLOSE THE FILE OF " + filePath);
				}
			}
			monitor.done();
		}
		
		private String getContent(final HSSFRow row, final int cellIndex) {
			HSSFCell cell = row.getCell((short) cellIndex);
			if (null == cell) {
				return Constants.EMPTY_STRING;
			}
			if (HSSFCell.CELL_TYPE_STRING != cell.getCellType()) {
				return changeToString(cell);
			}
			return cell.getRichStringCellValue().getString();
		}
		
		private String changeToString(final HSSFCell cell) {
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: 
				return String.valueOf(cell.getNumericCellValue());
			case HSSFCell.CELL_TYPE_BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());
			case HSSFCell.CELL_TYPE_ERROR: 
				return String.valueOf(cell.getErrorCellValue());
			case HSSFCell.CELL_TYPE_FORMULA:
				return String.valueOf(cell.getCellFormula());
			default:
				return Constants.EMPTY_STRING;
			}
		}
		
		private void createMessageMapToColumn(final HSSFRow row, final Map<String, Integer> messageFileMap) {
			// from second cell begin
			int result = 1;
			while (null != row.getCell((short) result)) {
				String messageFile = getContent(row, result);
				if (!Utils.isEmpty(messageFile)) {
					messageFileMap.put(messageFile, result);
				}
				result++;
			}
		}
		
		private void createMessageValue(final HSSFSheet sheet, final Map<String, Integer> messageFileMap, final IProgressMonitor monitor) {
			for (int iRow = 1; iRow < sheet.getLastRowNum(); iRow++) {
				if (monitor.isCanceled()) {
					break;
				}
				HSSFRow rowContent = sheet.getRow(iRow);
				if (null != rowContent) {
					String key = getContent(rowContent, 0);
					Map<String, String> cellMap = new HashMap<String, String>();
					int iColumnPos = 0;
					for (Map.Entry<String, Integer> me: messageFileMap.entrySet()) {
						int iColumn = me.getValue();
						String cellContent = getContent(rowContent, iColumn);
						cellMap.put(me.getKey(), cellContent);
						iColumnPos = iColumn++;
					}
					excelMap.put(key, cellMap);
					String cellContent = getContent(rowContent, iColumnPos);
					commentMap.put(key, cellContent);
				}
				monitor.worked(iCurWork++);
			}
		}
		
		private void createTreeNodes(final IProgressMonitor monitor) {
			for (Map.Entry<String, Map<String, String>> me: excelMap.entrySet()) {
				if (monitor.isCanceled()) {
					break;
				}
				INode node = null;
				boolean hasChild = root.hasChild(me.getKey());
				if (hasChild) {
					node = root.getChild(me.getKey());
				} else {
					node = new TreeNode(me.getKey(), Type.LEAF);
				}
				for (Map.Entry<String, String> mee: me.getValue().entrySet()) {
					String filename = mee.getKey();
					String value = mee.getValue();
					ValueEntity valueEntity = new ValueEntity();
					valueEntity.setCurrent(value);
					valueEntity.setOriginal(value);
					((TreeNode) node).getValueMap().put(filename, valueEntity);
					if (Utils.isEmpty(value)) {
						node = new TreeNode(me.getKey(), Type.LEAF, Status.LOAD_NEED_COMPLETE);
					}
				}
				((TreeNode) node).setComment(commentMap.get(me.getKey()));
				if (!hasChild) {
					root.addChild(node);
					NodeAssistant.getInstance().addTreeNodeChangeListener(node);
				}
				monitor.worked(iCurWork++);
			}
		}
	}
	
}
