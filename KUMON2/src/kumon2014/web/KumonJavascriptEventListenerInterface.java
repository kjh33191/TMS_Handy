package kumon2014.web;

import java.util.EventListener;

public interface KumonJavascriptEventListenerInterface extends EventListener {
	/**
	 * ���O�C���{�^���N���b�N���̃R�[���o�b�N
	 * @param id
	 * @param pswd
	 */
	public void onLogIn(String id, String pswd);
	/**
	 * �߂�{�^���N���b�N���̃R�[���o�b�N
	 */
	public void onBack();
	/**
	 * �s��
	 * @param kyozaiId
	 * @param printSetId
	 * @param printUnitId
	 */
	public void onDisplayPrint(String kyozaiId, String printSetId, String printUnitId);
}
