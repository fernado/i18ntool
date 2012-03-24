/**
 * Write @See TreeNode to the Excel
 * @author fernado
 * @date Jan 7, 2011
 */
package i18ntool.file.impl;

import i18ntool.consts.Filter;
import i18ntool.entity.TreeNode;
import i18ntool.entity.ValueEntity;
import i18ntool.file.ICommand;
import i18ntool.util.LogManager;
import i18ntool.util.MessageAssistant;
import i18ntool.util.NodeAssistant;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.consts.Type;
import iceworld.fernado.entity.INode;
import iceworld.fernado.file.ISaveResource;
import iceworld.fernado.util.Utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;


public class SaveExcel implements ISaveResource {

	private static final String ID = SaveExcel.class.getName();
	private static final Logger log = LogManager.getLogger(ID);
	// declare a workbook
	private final HSSFWorkbook wb = new HSSFWorkbook();
	// create a worksheet
	private final HSSFSheet sheet = wb.createSheet("i18nTool");
	private String file;
	private Filter filter;
	
	public SaveExcel(final String file, final Filter filter) {
		this.file = file;
		this.filter = filter;
		init();
	}
	
	private void init() {
		initSheet();
		initNormalFont();
		initMessageStyle();
		initKeyStyle();
		initNormalStyle();
		initChangedStyle();
		initEmptyStyle();
		initChangedOrEmptyStyle();
	}
	
	private HSSFFont normalFont = null;
	private HSSFCellStyle messageStyle = null;
	private HSSFCellStyle keyStyle = null;
	private HSSFCellStyle normalStyle = null;
	private HSSFCellStyle changedStyle = null;
	private HSSFCellStyle emptyStyle = null;
	private HSSFCellStyle changedOrEmptyStyle = null;
	
	private void initSheet() {
		String[] messageFiles = MessageAssistant.getInstance().getMessageFiles();
		sheet.setColumnWidth((short) 0, (short) 10000);
		
		for (short iColumn = 1; iColumn <= messageFiles.length; iColumn++) {
			sheet.setColumnWidth(iColumn, (short) 12000);
		}
	}
	
	private void initNormalFont() {
		normalFont = wb.createFont();
		normalFont.setFontName("Arial");
		normalFont.setFontHeightInPoints((short) 8);
	}
	
	private void initMessageStyle() {
		messageStyle = wb.createCellStyle();
		messageStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		messageStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		messageStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		messageStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		messageStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		messageStyle.setWrapText(true);
		messageStyle.setFont(normalFont);
	}
	
	private void initKeyStyle() {
		keyStyle = wb.createCellStyle();
		keyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		keyStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		keyStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		keyStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		keyStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		keyStyle.setWrapText(true);
		keyStyle.setFont(normalFont);
	}
	
	
	private void initNormalStyle() {
		normalStyle = wb.createCellStyle();
		normalStyle.setFillForegroundColor(HSSFColor.WHITE.index);
		normalStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		normalStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		normalStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		normalStyle.setWrapText(true);
		normalStyle.setFont(normalFont);
	}
	
	private void initChangedStyle() {
		changedStyle = wb.createCellStyle();
		changedStyle.setFillForegroundColor(HSSFColor.ORANGE.index);
		changedStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		changedStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		changedStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		changedStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		changedStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		changedStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		changedStyle.setWrapText(true);
		changedStyle.setFont(normalFont);
	}
	
	private void initEmptyStyle() {
		emptyStyle = wb.createCellStyle();
		emptyStyle.setFillForegroundColor(HSSFColor.RED.index);
		emptyStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		emptyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		emptyStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		emptyStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		emptyStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		emptyStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	}
	
	private void initChangedOrEmptyStyle() {
		changedOrEmptyStyle = wb.createCellStyle();
		changedOrEmptyStyle.setFillForegroundColor(HSSFColor.PINK.index);
		changedOrEmptyStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		changedOrEmptyStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		changedOrEmptyStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		changedOrEmptyStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		changedOrEmptyStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		changedOrEmptyStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		changedOrEmptyStyle.setWrapText(true);
		changedOrEmptyStyle.setFont(normalFont);
	}
	
	@Override
	public void removeResources() throws IOException {
		Utils.removeFiles(file);
		log.log(Level.INFO, ID + ".removeResources() finish");
	}
	
	@Override
	public void saveResources() throws IOException, URISyntaxException {
		String[] messageFiles = MessageAssistant.getInstance().getMessageFiles();
		log.log(Level.INFO, "SAVE EXCEL TO FILE -- " + file);
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(Display
				.getCurrent().getActiveShell());
		try {
			progressMonitorDialog.run(false, true, new SavingProgress(messageFiles));
		} catch (InvocationTargetException e) {
			log.log(Level.WARNING, "SAVE PROPERTIES TO EXCEL -- INVOCATION TARGET EXCEPTION");
		} catch (InterruptedException e) {
			log.log(Level.WARNING, "SAVE PROPERTIES TO EXCEL -- INTERRUPTED EXCEPTION");
		}
	}
	
	private void doWriting() throws IOException, URISyntaxException {
		log.log(Level.INFO, "SAVE PROPERTIES TO EXCEL -- " + file);
		writeToExcel();
	}
	
	private class SavingProgress implements IRunnableWithProgress {

		private String[] messageFiles;

		public SavingProgress(final String[] messageFiles) {
			this.messageFiles = messageFiles;
		}

		public void run(final IProgressMonitor monitor) throws InvocationTargetException,
				InterruptedException {
			monitor.beginTask("Saving Excel to File", IProgressMonitor.UNKNOWN);
			Job job = new ActivateJob("Saving Excel to File");
			job.addJobChangeListener(new ActivateJobChangeAdapter());
			job.setUser(true);
			job.join();
			job.schedule();
			if (monitor.isCanceled()) {
				throw new InterruptedException("The long running operation was cancelled");
			}
		}

		private class ActivateJob extends Job {
			public ActivateJob(final String name) {
				super(name);
			}

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				int work = messageFiles.length;
				double workRate = initWorkRate(work);
				initHeader(messageFiles);
				try {
					initBody(messageFiles);
					monitor.worked(getWorked(workRate, work - 1));
					doWriting();
					monitor.worked(getWorked(workRate, work));
					return new Status(Status.OK, "OK", "OK");
				} catch (Exception ex) {
					return new Status(Status.ERROR, "FAIL", "FAIL TO SAVE EXCEL, MAKE SURE THE EXCEL IS NOT OPEN");
				}
			}
			
			private double initWorkRate(final int work) {
				return 100 / work;
			}
			
			private int getWorked(final double workRate, final int index) {
				return ((int) workRate * index);
			}
		}

		private class ActivateJobChangeAdapter extends JobChangeAdapter {

			@Override
			public void done(final IJobChangeEvent event) {
				IStatus iStatus = event.getResult();
				String message = iStatus.getMessage();

				if (IStatus.OK != iStatus.getSeverity()) {
					log.log(Level.WARNING, "This action's status :" + message);
				}
			}
		}
	}
	
	/**
	 * init header
	 * like: message.properties     message_en_MY.properties
	 * @param messageFiles
	 */
	private void initHeader(final String[] messageFiles) {
		// create a column
		HSSFRow row = sheet.createRow(0);
		
		// declare a cell
		HSSFCell firstCell = row.createCell((short) 0);
		// give the value of the cell
		firstCell.setCellValue(new HSSFRichTextString("KEYS"));
		firstCell.setCellStyle(messageStyle);
		short i = 1;
		short j = 0;
		
		for (; i <= messageFiles.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(new HSSFRichTextString(messageFiles[j++]));
			cell.setCellStyle(messageStyle);
		}
		HSSFCell cell = row.createCell(i);
		cell.setCellValue(new HSSFRichTextString("Comment"));
		cell.setCellStyle(messageStyle);
	}
	
	private void initBody(final String[] messageFiles) {
		switch (filter) {
		case NORMAL:
		case NOT_EMPTY_VALUE:
		case NOT_EMPTY_VALUE_BUT_ALLOW_ORIGINAL_VALUE_EMPTY:
			new NormalBoyCommand(messageFiles).execute();
			break;
		case CHANGED_OR_EMPTY:
		case CHANGED_OR_EMPTY_OR_NORMAL:
			new ChangedOrEmptyBodyCommand(messageFiles).execute();
			break;
		case CHANGED:
		case CHANGED_OR_NORMAL:
			new ChangedBodyCommand(messageFiles).execute();
			break;
		case EMPTY:
		case EMPTY_OR_NORMAL:
			new EmptyBodyCommand(messageFiles).execute();
			break;
		default:
			break;
		}
	}
	
	/**
	 * put the node's value which is changed/empty/normal to the excel object
	 * 
	 * Note: changed/empty will not be high light
	 * @author fernado
	 *
	 */
	private class NormalBoyCommand implements ICommand {
		private String COMMAND_ID = NormalBoyCommand.class.getName();
		private String[] messages;
		public NormalBoyCommand(final String[] messages) {
			this.messages = messages;
		}
		@Override
		public void execute() {
			log.log(Level.INFO, COMMAND_ID + ".execute() start");
			Map<String, Map<String, String>> map = createNormalBody(messages);
			int iRow = 1;
			for (Map.Entry<String, Map<String, String>> me: map.entrySet()) {
				HSSFRow row = sheet.createRow(iRow++);
				HSSFCell firstCell = row.createCell((short) 0);
				firstCell.setCellValue(new HSSFRichTextString(me.getKey()));
				firstCell.setCellStyle(keyStyle);
				short iColumn = 1;
				for (Map.Entry<String, String> mee: me.getValue().entrySet()) {
					HSSFCell cell = row.createCell(iColumn);
					cell.setCellValue(new HSSFRichTextString(mee.getValue()));
					cell.setCellStyle(normalStyle);
					iColumn++;
				}
				String comment = NodeAssistant.getInstance().getKeyComment(me.getKey());
				HSSFCell cell = row.createCell(iColumn);
				cell.setCellValue(new HSSFRichTextString(comment));
				cell.setCellStyle(normalStyle);
			}
			log.log(Level.INFO, COMMAND_ID + ".execute() end");
		}
		
	}
	
	/**
	 * <pre>
	 * If createChangedOrEmptyBody(messageFiles) is not empty and global filter is CHANGED_OR_EMPTY_OR_NORMAL
	 * then put the node's value which is changed/empty/normal to the excel object
	 * else put the node's value which is changed/empty to the excel object
	 * </pre>
	 * Note: changed/empty will be high light
	 * 
	 * @author fernado
	 *
	 */
	private class ChangedOrEmptyBodyCommand implements ICommand {
		private String COMMAND_ID = ChangedOrEmptyBodyCommand.class.getName();
		private String[] messages;
		public ChangedOrEmptyBodyCommand(final String[] messages) {
			this.messages = messages;
		}
		@Override
		public void execute() {
			log.log(Level.INFO, COMMAND_ID + ".execute() start");
			Map<String, Map<String, String>> changedOrEmptyMap = createChangedOrEmptyBody(messages);
			Map<String, Map<String, String>> normalMap = createNormalBody(messages);
			int iRow = 1;
			for (Map.Entry<String, Map<String, String>> me: normalMap.entrySet()) {
				Map<String, String> changedOrEmptyValues = changedOrEmptyMap.get(me.getKey());
				if (Utils.isEmpty(changedOrEmptyValues) && Filter.CHANGED_OR_EMPTY_OR_NORMAL != filter) {
					continue;
				}
				HSSFRow row = sheet.createRow(iRow++);
				HSSFCell firstCell = row.createCell((short) 0);
				firstCell.setCellValue(new HSSFRichTextString(me.getKey()));
				firstCell.setCellStyle(keyStyle);
				short iColumn = 1;
				for (Map.Entry<String, String> mee: me.getValue().entrySet()) {
					HSSFCell cell = row.createCell(iColumn);
					cell.setCellValue(new HSSFRichTextString(mee.getValue()));
					cell.setCellStyle(normalStyle);
					iColumn++;
					if (Utils.isEmpty(changedOrEmptyValues)) {
						continue;
					} else if (changedOrEmptyValues.containsKey(mee.getKey())) {
						cell.setCellStyle(Utils.isEmpty(mee.getValue()) ? emptyStyle : changedStyle);
					} 
				}
				String comment = NodeAssistant.getInstance().getKeyComment(me.getKey());
				HSSFCell cell = row.createCell(iColumn);
				cell.setCellValue(new HSSFRichTextString(comment));
			}
			log.log(Level.INFO, COMMAND_ID + ".execute() end");
		}
		
	}
	
	/**
	 * if createChangedBody(messageFiles) is not empty and global filter is CHANGED_OR_NORMAL
	 * then put the node's value which is changed/normal to the excel object
	 * else put the node's value which is changed to the excel object
	 *
	 * Note: changed will be high light
	 * @author fernado
	 *
	 */
	private class ChangedBodyCommand implements ICommand {
		private String COMMAND_ID = ChangedBodyCommand.class.getName();
		private String[] messages;
		public ChangedBodyCommand(final String[] messages) {
			this.messages = messages;
		}
		@Override
		public void execute() {
			log.log(Level.INFO, COMMAND_ID + ".execute() start");
			Map<String, Map<String, String>> changedMap = createChangedBody(messages);
			Map<String, Map<String, String>> normalMap = createNormalBody(messages);
			int iRow = 1;
			for (Map.Entry<String, Map<String, String>> me: normalMap.entrySet()) {
				Map<String, String> changedValues = changedMap.get(me.getKey());
				if (changedValues.isEmpty() && Filter.CHANGED_OR_NORMAL != filter) {
					continue;
				}
				HSSFRow row = sheet.createRow(iRow++);
				HSSFCell firstCell = row.createCell((short) 0);
				firstCell.setCellValue(new HSSFRichTextString(me.getKey()));
				firstCell.setCellStyle(keyStyle);
				short iColumn = 1;
				for (Map.Entry<String, String> mee: me.getValue().entrySet()) {
					HSSFCell cell = row.createCell(iColumn);
					cell.setCellValue(new HSSFRichTextString(mee.getValue()));
					cell.setCellStyle(changedValues.containsKey(mee.getKey()) ? changedStyle : normalStyle);
					iColumn++;
				}
				String comment = NodeAssistant.getInstance().getKeyComment(me.getKey());
				HSSFCell cell = row.createCell(iColumn);
				cell.setCellValue(new HSSFRichTextString(comment));
			}
			log.log(Level.INFO, COMMAND_ID + ".execute() end");
		}
	}
	
	/**
	 * if createEmptyBody(messageFiles) is not empty and global filter is EMPTY_OR_NORMAL
	 * then put the node's value which is empty/normal to the excel object
	 * else put the node's value which is empty to the excel object
	 * 
	 * Note: empty will be high light
	 * @author fernado
	 *
	 */
	private class EmptyBodyCommand implements ICommand {
		private String COMMAND_ID = EmptyBodyCommand.class.getName();
		private String[] messages;
		public EmptyBodyCommand(final String[] messages) {
			this.messages = messages;
		}
		@Override
		public void execute() {
			log.log(Level.INFO, COMMAND_ID + ".execute() start");
			Map<String, Map<String, String>> emptyMap = createEmptyBody(messages);
			Map<String, Map<String, String>> normalMap = createNormalBody(messages);
			int iRow = 1;
			for (Map.Entry<String, Map<String, String>> me: normalMap.entrySet()) {
				Map<String, String> emptyValues = emptyMap.get(me.getKey());
				if (emptyValues.isEmpty() && Filter.EMPTY_OR_NORMAL != filter) {
					continue;
				}
				HSSFRow row = sheet.createRow(iRow++);
				HSSFCell firstCell = row.createCell((short) 0);
				firstCell.setCellValue(new HSSFRichTextString(me.getKey()));
				firstCell.setCellStyle(keyStyle);
				short iColumn = 1;
				for (Map.Entry<String, String> mee: me.getValue().entrySet()) {
					HSSFCell cell = row.createCell(iColumn);
					cell.setCellValue(new HSSFRichTextString(mee.getValue()));
					cell.setCellStyle(emptyValues.containsKey(mee.getKey()) ? emptyStyle : normalStyle);
					iColumn++;
				}
				String comment = NodeAssistant.getInstance().getKeyComment(me.getKey());
				HSSFCell cell = row.createCell(iColumn);
				cell.setCellValue(new HSSFRichTextString(comment));
			}
			log.log(Level.INFO, COMMAND_ID + ".execute() end");
		}
	}
	
	/**
	 * record all the node's status is normal
	 * @param messageFiles
	 * @return <key, Map<language, Excel-Entity>>
	 */
	private Map<String, Map<String, String>> createNormalBody(final String[] messageFiles) {
		Map<String, Map<String, String>> resultMap = new TreeMap<String, Map<String, String>>();
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			Map<String, String> cellMap = new LinkedHashMap<String, String>();
			if (Type.LEAF == node.getType()) {
				Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
				for (String messageFile : messageFiles) {
					if (valueMap.isEmpty()) {
						cellMap.put(messageFile, Constants.EMPTY_STRING);
					}
					if (null == valueMap.get(messageFile)
							|| Constants.EMPTY_STRING.equals(valueMap.get(messageFile).getCurrent())) {
						cellMap.put(messageFile, Constants.EMPTY_STRING);
					} else {
						cellMap.put(messageFile, valueMap.get(messageFile).getCurrent());
					}
				}
			}
			resultMap.put(node.getName(), cellMap);
		}
		return resultMap;
	}
	
	/**
	 * record all the node's status is empty
	 * and 
	 * @param messageFiles
	 * @return <key, Map<language, Excel-Entity>>
	 */
	private Map<String, Map<String, String>> createEmptyBody(final String[] messageFiles) {
		Map<String, Map<String, String>> resultMap = new TreeMap<String, Map<String, String>>();
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			Map<String, String> cellMap = new LinkedHashMap<String, String>();
			if (Type.LEAF == node.getType()) {
				if (i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.LOAD_NEED_COMPLETE == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY == ((TreeNode) node).getStatus()) {
					Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
					for (String messageFile : messageFiles) {
						if (valueMap.isEmpty()) {
							cellMap.put(messageFile, Constants.EMPTY_STRING);
						} else if (null == valueMap.get(messageFile)
								|| Constants.EMPTY_STRING.equals(valueMap.get(messageFile).getCurrent())) {
							cellMap.put(messageFile, Constants.EMPTY_STRING);
						}
					}
				}
			}
			resultMap.put(node.getName(), cellMap);
		}
		return resultMap;
	}
	
	/**
	 * record all the node's status is changed
	 * @param messageFiles
	 * @return <key, Map<language, Excel-Entity>>
	 */
	private Map<String, Map<String, String>> createChangedBody(final String[] messageFiles) {
		Map<String, Map<String, String>> resultMap = new TreeMap<String, Map<String, String>>();
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			Map<String, String> cellMap = new LinkedHashMap<String, String>();
			if (Type.LEAF == node.getType()) {
				if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatus()) {
					Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
					for (String messageFile : messageFiles) {
						if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatus()) {
							cellMap.put(messageFile, valueMap.get(messageFile).getCurrent());
						}
						if (null != ((TreeNode) node).getStatusMap().get(messageFile)) {
							if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatusMap().get(messageFile)) {
								cellMap.put(messageFile, valueMap.get(messageFile).getCurrent());
							} else if (i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatusMap().get(messageFile)) {
								cellMap.put(messageFile, Constants.EMPTY_STRING);
							}
						}
					}
				}
			}
			resultMap.put(node.getName(), cellMap);
		}
		return resultMap;
	}
	
	/**
	 * create a map when the node's status is changed or empty
	 * 
	 * 
	 * @param messageFiles
	 * @return
	 */
	private Map<String, Map<String, String>> createChangedOrEmptyBody(final String[] messageFiles) {
		Map<String, Map<String, String>> resultMap = new TreeMap<String, Map<String, String>>();
		for (INode node : NodeAssistant.getInstance().getData().getChildren()) {
			Map<String, String> cellMap = new LinkedHashMap<String, String>();
			if (Type.LEAF == node.getType()) {
				if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.LOAD_NEED_COMPLETE == ((TreeNode) node).getStatus()
						|| i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY == ((TreeNode) node).getStatus()) {
					Map<String, ValueEntity> valueMap = ((TreeNode) node).getValueMap();
					for (String messageFile : messageFiles) {
						if (valueMap.isEmpty()) {
							cellMap.put(messageFile, Constants.EMPTY_STRING);
						} else if (null == valueMap.get(messageFile)
							|| Constants.EMPTY_STRING.equals(valueMap.get(messageFile).getCurrent())) {
							cellMap.put(messageFile, Constants.EMPTY_STRING);
						} else if (i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatus()) {
							if (i18ntool.consts.Status.SAVE_NEED_COMPLETE == ((TreeNode) node).getStatusMap().get(messageFile)) {
								cellMap.put(messageFile, Constants.EMPTY_STRING);
							} else if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatusMap().get(messageFile)) {
								cellMap.put(messageFile, valueMap.get(messageFile).getCurrent());
							}
						} else if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatus()) {
							cellMap.put(messageFile, valueMap.get(messageFile).getCurrent());
						} else if ( i18ntool.consts.Status.LOAD_NEED_COMPLETE == ((TreeNode) node).getStatus()) {
							if (i18ntool.consts.Status.LOAD_NEED_COMPLETE == ((TreeNode) node).getStatusMap().get(messageFile)) {
								cellMap.put(messageFile, Constants.EMPTY_STRING);
							}
						} else if (i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY == ((TreeNode) node).getStatus()) {
							if (i18ntool.consts.Status.CHANGED == ((TreeNode) node).getStatusMap().get(messageFile)) {
								cellMap.put(messageFile, valueMap.get(messageFile).getCurrent());
							}
						}
					}
					resultMap.put(node.getName(), cellMap);
				}
			}
		}
		return resultMap;
	}
	
	
	private void writeToExcel() throws FileNotFoundException, IOException {
		log.log(Level.INFO, ID + "writeToExcel() start");
		FileOutputStream fout = null;
		Utils.createFolderAndFile(MessageAssistant.getInstance().getFolder(), file);
		try {
			fout = new FileOutputStream(file);
			wb.write(fout);
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				log.log(Level.WARNING, "CANT CLOSE THE FILE OF " + file);
			}
		}
		log.log(Level.INFO, ID + "writeToExcel() end");
	}
	
}
