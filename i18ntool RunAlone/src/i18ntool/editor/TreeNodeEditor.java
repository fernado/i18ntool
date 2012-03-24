/**
 * 
 * @author fernado  
 * @date 28/07/2010
 */
package i18ntool.editor;

import i18ntool.consts.Status;
import i18ntool.entity.InputEntity;
import i18ntool.entity.TreeNode;
import i18ntool.entity.ValueEntity;
import i18ntool.property.Resource;
import i18ntool.util.MessageAssistant;
import iceworld.fernado.consts.Constants;
import iceworld.fernado.util.Utils;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class TreeNodeEditor extends EditorPart {

	public static final String ID = TreeNodeEditor.class.getName();
	
	private static final Logger log = Logger.getLogger(ID);
	
	private Composite txtValueContainer;
	private Composite txtKeyContainer;

	private Label[] lblCountries;
	private Button[] chkCountries;

	private Text[] txtValues;

	private Text txtKey;
	
	private Text txtComment;
	
	private boolean dirty;
	
	private String[] lastValues;
	
//	private String lastComment;

	private static final Font boldFont = JFaceResources.getFontRegistry().getBold(
			JFaceResources.DEFAULT_FONT);
	private static final Font italicFont = JFaceResources.getFontRegistry().getItalic(
			JFaceResources.DEFAULT_FONT);
	
	private static final Color keyBGColor = new Color(Display.getDefault(), 88, 184, 71);
	private static final Color txtBGColor = new Color(Display.getDefault(), 200, 200, 0);
	private static final Color txtCommentBGColor = new Color(Display.getDefault(), 5, 200, 64);

	@Override
	public void doSave(final IProgressMonitor monitor) {
		log.log(Level.INFO, "Editor - doSave(IProgressMonitor monitor)");
		saveData(false);
		setDirty(false);
	}

	/**
	 * 
	 * @param isClosed
	 * 
	 *            if isClosed == true, this function is for editor close action
	 *            else is Closed == false, this function is for editor save
	 *            action
	 */
	private void saveData(final boolean isClosed) {
		final TreeNodeEditorInput input = (TreeNodeEditorInput) this.getEditorInput();
		final InputEntity entity = input.getEntity();
		final TreeNode node = (TreeNode) input.getNode();
		boolean empty = false;
		boolean changed = false;
		
		if (emptyRecordExists(entity)) {
			node.setStatus(i18ntool.consts.Status.SAVE_NEED_COMPLETE);
			empty = true;
		}
		
		if (!Utils.isSame(entity.getCurrentKey(), entity.getOriginalKey())) {
			setPartName(entity.getCurrentKey());
			node.setName(entity.getCurrentKey());
			node.setStatus(i18ntool.consts.Status.CHANGED, true);
		}
		
		if (!Utils.isSame(entity.getCurrentValues(), entity.getOriginalValues())) {
			setStatusMap(entity.getCurrentValues(), entity.getOriginalValues());
			for (Map.Entry<String, Status> me: node.getStatusMap().entrySet()) {
				if (i18ntool.consts.Status.CHANGED == me.getValue()) {
					changed = true;
					node.setStatus(i18ntool.consts.Status.CHANGED);
					break;
				}
			}
		}
		
		if (!Utils.isSame(entity.getCurrentComment(), entity.getOriginalComment())) {
			changed = true;
			node.setComment(entity.getCurrentComment());
			node.setStatus(i18ntool.consts.Status.CHANGED);
		}
		
		if (empty && changed) {
			node.setStatus(i18ntool.consts.Status.SAVE_CHANGED_OR_EMPTY);
			setStatusMap(entity.getCurrentValues(), entity.getOriginalValues());
		}
		
		String[] messageFiles = MessageAssistant.getInstance().getMessageFiles();
		for (int i = 0; i < entity.getCurrentValues().length; i++) {
			String value = entity.getCurrentValues()[i];
			Map<String, ValueEntity> valueMap = node.getValueMap();
			ValueEntity valueEntity = valueMap.get(messageFiles[i]);
			valueEntity.setCurrent(value);
			valueMap.put(messageFiles[i], valueEntity);
			if (!isClosed) {
				entity.getOriginalValues()[i] = value;
			}
		}
	}
	
	private boolean emptyRecordExists(final InputEntity entity) {
		for (int i = 0; i < entity.getCurrentValues().length; i++) {
			if (Constants.EMPTY_STRING.equals(entity.getCurrentValues()[i])) {
				return true;
			}
		}
		return false;
	}
	
	private void setStatusMap(final String[] strs1, final String[] strs2) {
		final TreeNodeEditorInput input = (TreeNodeEditorInput) this.getEditorInput();
		final TreeNode node = (TreeNode) input.getNode();
		Map<String, i18ntool.consts.Status> resultMap = node.getStatusMap();
		for (int i = 0; i < strs1.length; i++) {
			if (!Utils.isSame(strs1[i], strs2[i])) {
				if (Utils.isEmpty(strs1[i])) {
					resultMap.put(MessageAssistant.getInstance().getMessageFiles()[i], i18ntool.consts.Status.SAVE_NEED_COMPLETE);
				} else {
					resultMap.put(MessageAssistant.getInstance().getMessageFiles()[i], i18ntool.consts.Status.CHANGED);
				}
			}
		}
		node.setStatusMap(resultMap);
	}
	
	@Override
	public void doSaveAs() {
		doSave(null);
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		if (null != input) {
			this.setPartName(input.getName());
		}
		this.setInput(input);
		this.setSite(site);
	}

	@Override
	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(final boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(final Composite parent) {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		parent.setLayout(gridLayout);
		GridData data = new GridData(GridData.FILL_BOTH);
		parent.setLayoutData(data);
		txtKeyContainer = new Composite(parent, SWT.NONE);
		txtValueContainer = new Composite(parent, SWT.NONE);
		initTxtKeyContainer();
		initTxtValueContainer();
	}
	
	private void initTxtKeyContainer() {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		txtKeyContainer.setLayout(gridLayout);
		txtKeyContainer.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		txtKeyContainer.setBackground(keyBGColor);
		initKeyText();
	}

	private void initKeyText() {
		TreeNodeEditorInput input = (TreeNodeEditorInput) this.getEditorInput();
		final InputEntity entity = input.getEntity();
		final GridData gdTxt = new GridData(GridData.FILL_BOTH);
		txtKey = new Text(txtKeyContainer, SWT.FLAT | SWT.SINGLE);
		txtKey.setBackground(keyBGColor);
		String text = Utils.isEmpty(input.getName()) ? Constants.EMPTY_STRING : input.getName();
		txtKey.setLayoutData(gdTxt);
		txtKey.setText(text);
		txtKey.setFont(italicFont);
		entity.setCurrentKey(text);
		entity.setOriginalKey(text);
		if (!Utils.isEmpty(text)) {
			txtKey.setEditable(false);
		}
	}

	private void initCommentText() {
		TreeNodeEditorInput input = (TreeNodeEditorInput) this.getEditorInput();
		final InputEntity entity = input.getEntity();
		final TreeNode node = (TreeNode) input.getNode();
		final GridData gdTxt = new GridData(GridData.FILL_BOTH);
		final GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gdTxt.horizontalSpan = 6;
		
		Label lblComment = new Label(txtValueContainer, SWT.NONE);
		lblComment.setText(Constants.COMMENT);
		lblComment.setBackground(txtBGColor);
		lblComment.setLayoutData(gd);
		lblComment.setFont(boldFont);
		
		txtComment = new Text(txtValueContainer, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		String text = Utils.isEmpty(node.getComment()) ? Constants.EMPTY_STRING : node.getComment();
		txtComment.setForeground(txtCommentBGColor);
		txtComment.setLayoutData(gdTxt);
		txtComment.setText(text);
		txtComment.setEditable(true);
		
		entity.setCurrentComment(text);
		entity.setOriginalComment(text);
//		lastComment = text;
		txtComment.addModifyListener(new TextCommentModifyListener());
	}

	private void initTxtValueContainer() {
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 6;
		txtValueContainer.setLayout(gridLayout);
		txtValueContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		txtValueContainer.setBackground(txtBGColor);
		dynamicValueText();
	}

	private void dynamicValueText() {
		initCommentText();
		
		TreeNodeEditorInput input = (TreeNodeEditorInput) this.getEditorInput();
		final TreeNode node = (TreeNode) input.getNode();
		final InputEntity entity = input.getEntity();
		String[] messageFiles = MessageAssistant.getInstance().getMessageFiles();
		lastValues = new String[MessageAssistant.getInstance().getMessageFiles().length];
		
		lblCountries = new Label[messageFiles.length];
		chkCountries = new Button[messageFiles.length];
		txtValues = new Text[messageFiles.length];

		String[] currentValues = new String[messageFiles.length];
		String[] originalValues = new String[messageFiles.length];
		Utils.initArrayOfStringIfNull(currentValues);
		Utils.initArrayOfStringIfNull(originalValues);

		entity.setCurrentValues(currentValues);
		entity.setOriginalValues(originalValues);

		final GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		final GridData gdValue = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		gdValue.horizontalSpan = 6;
		
		for (int i = 0; i < messageFiles.length; i++) {
			lblCountries[i] = new Label(txtValueContainer, SWT.NONE);
			lblCountries[i].setFont(boldFont);
			lblCountries[i].setText(messageFiles[i]);
			lblCountries[i].setBackground(txtBGColor);
			Label lblLeftParenthesis = new Label(txtValueContainer, SWT.NONE);
			lblLeftParenthesis.setText(Constants.LEFT_PARENTHESIS);
			lblLeftParenthesis.setBackground(txtBGColor);
			chkCountries[i] = new Button(txtValueContainer, SWT.CHECK);
			chkCountries[i].setText(Resource.CLEAR_THE_CONTENT);
			chkCountries[i].setBackground(txtBGColor);
			chkCountries[i].setLayoutData(gd);
			Label lblRightParenthesis = new Label(txtValueContainer, SWT.NONE);
			lblRightParenthesis.setText(Constants.RIGHT_PARENTHESIS);
			lblRightParenthesis.setBackground(txtBGColor);
			txtValues[i] = new Text(txtValueContainer, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
			txtValues[i].setLayoutData(gdValue);
			txtValues[i].setEditable(true);
			txtValues[i].setToolTipText(Resource.SHOW_DO_NOT_TYPE_ENTER_KEY_AND_USE_R_N);

			String text = Constants.EMPTY_STRING;

			Map<String, ValueEntity> valueMap = node.getValueMap();
			ValueEntity valueEntity = valueMap.get(messageFiles[i]);
			if (null != valueMap.get(messageFiles[i])) {
				text = Utils.isEmpty(valueEntity.getCurrent()) ? Constants.EMPTY_STRING : valueEntity.getCurrent();
			}
			
			txtValues[i].setText(text);
			entity.getOriginalValues()[i] = text;
			entity.getCurrentValues()[i] = text;
			
			if (Utils.isEmpty(text)) {
				chkCountries[i].setSelection(true);
			}

			chkCountries[i].addSelectionListener(new CheckBoxSelectionListener(i));
//			txtValues[i].addTraverseListener(new ValuesTraverseListener(i));
			txtValues[i].addModifyListener(new TextValueModifyListener(i));
			
			lastValues[i] = text;
		}
	}

	private class CheckBoxSelectionListener extends SelectionAdapter {
		private int index;
		
		public CheckBoxSelectionListener(int index) {
			this.index = index;
		}

		@Override
		public void widgetSelected(final SelectionEvent e) {
			if (e.getSource() instanceof Button) {
				Button checkbox = (Button) e.getSource();
				if (checkbox.getSelection()) {
					txtValues[index].setText(Constants.EMPTY_STRING);		
				}
				
				if (Utils.isEmpty(txtValues[index].getText())) {
					checkbox.setSelection(true);
				}
			}
		}
	}
	
//	private class ValuesTraverseListener implements TraverseListener {
//		private int index;
//
//		public ValuesTraverseListener(int index) {
//			this.index = index;
//		}
//		
//		@Override
//		public void keyTraversed(TraverseEvent e) {
//			if (SWT.KEYPAD_CR  == e.keyCode || SWT.CR == e.keyCode) {
//				txtValues[index].setText(lastValues[index]);
//			} else {
//				lastValues[index] = txtValues[index].getText();
//			}
//		}
//	}
	
//	private class CommentTraverseListener implements TraverseListener {
//		
//		@Override
//		public void keyTraversed(TraverseEvent e) {
//			if (SWT.KEYPAD_CR == e.keyCode || SWT.CR == e.keyCode
//					|| SWT.LF == e.keyCode) {
//				txtComment.setTextLimit(Text.LIMIT);
//			} else {
//				lastComment = txtComment.getText();
//				txtComment.addModifyListener(new TextCommentModifyListener());
//			}
//		}
//	}
	
	private class TextCommentModifyListener implements ModifyListener {

		public TextCommentModifyListener() {
		}

		@Override
		public void modifyText(ModifyEvent e) {
			if (e.getSource() instanceof Text) {
				Text value = (Text) e.getSource();
				TreeNodeEditorInput input = (TreeNodeEditorInput) getEditorInput();
				final InputEntity entity = input.getEntity();
				entity.setCurrentComment(value.getText());
				setDirty(!editorDirtyStateChanged());
			}
		}
	}
	
	private class TextValueModifyListener implements ModifyListener {
		private int index;

		public TextValueModifyListener(int index) {
			this.index = index;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			if (e.getSource() instanceof Text) {
				Text value = (Text) e.getSource();
				TreeNodeEditorInput input = (TreeNodeEditorInput) getEditorInput();
				final InputEntity entity = input.getEntity();
				entity.getCurrentValues()[index] = value.getText();
				if (!Utils.isEmpty(value.getText())) {
					chkCountries[index].setSelection(false);
				} else {
					chkCountries[index].setSelection(true);
				}
				setDirty(!editorDirtyStateChanged());
			}
		}
	}
	
	/**
	 * compare the current value and original value are the same or not
	 */
	private boolean editorDirtyStateChanged() {
		final TreeNodeEditorInput input = (TreeNodeEditorInput) this.getEditorInput();
		final InputEntity entity = input.getEntity();
		boolean changed = false;
		boolean commentChanged = false;
		for (int i = 0; i < entity.getCurrentValues().length; i++) {
			changed = Utils.isSame(entity.getCurrentValues()[i], entity.getOriginalValues()[i]);
			if (!changed) {
				break;
			}
		}
		
		commentChanged = Utils.isSame(entity.getCurrentComment(), entity.getOriginalComment());
		return changed && commentChanged;
	}

	@Override
	public void setFocus() {
		txtKey.setFocus();
	}
}
