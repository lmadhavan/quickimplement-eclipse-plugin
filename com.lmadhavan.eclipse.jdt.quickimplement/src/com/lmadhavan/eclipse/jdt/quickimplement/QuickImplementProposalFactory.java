package com.lmadhavan.eclipse.jdt.quickimplement;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.correction.ChangeCorrectionProposal;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.swt.graphics.Image;

/**
 * Factory for creating a Quick Implement proposal for a Java type.
 */
public class QuickImplementProposalFactory {
	private final Image proposalImage;
	
	public QuickImplementProposalFactory() {
		this.proposalImage = JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_CLASS);
	}

	public IJavaCompletionProposal createProposal(IType type) {
		Change change = new QuickImplementChange(type);
		return new ChangeCorrectionProposal(change.getName() + "...", change, 0, proposalImage);
	}
}
