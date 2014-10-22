package com.lmadhavan.eclipse.jdt.quickimplement;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;

/**
 * Contributes a Quick Implement proposal for interface and class declarations.
 */
public class QuickImplementProcessor implements IQuickAssistProcessor {
	private final QuickImplementProposalFactory proposalFactory;
	
	public QuickImplementProcessor() {
		this(new QuickImplementProposalFactory());
	}
	
	public QuickImplementProcessor(QuickImplementProposalFactory proposalFactory) {
		this.proposalFactory = proposalFactory;
	}

	@Override
	public IJavaCompletionProposal[] getAssists(IInvocationContext context, IProblemLocation[] locations) throws CoreException {
		IType type = findType(context);
		return type == null ? null : new IJavaCompletionProposal[] { proposalFactory.createProposal(type) };
	}

	@Override
	public boolean hasAssists(IInvocationContext context) throws CoreException {
		return findType(context) != null;
	}

	private IType findType(IInvocationContext context) {
		ASTNode node = context.getCoveringNode();
		
		if (node instanceof SimpleName && node.getParent() instanceof TypeDeclaration) {
			String typeName = ((SimpleName) node).getIdentifier();
			return context.getCompilationUnit().getType(typeName);
		}
		
		return null;
	}
}
