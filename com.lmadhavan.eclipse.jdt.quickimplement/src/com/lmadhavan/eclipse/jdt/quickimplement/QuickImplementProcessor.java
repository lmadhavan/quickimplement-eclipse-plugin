package com.lmadhavan.eclipse.jdt.quickimplement;

import java.lang.reflect.Modifier;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;
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
        ITypeBinding typeBinding = resolveTypeBinding(context);
        IType enclosingType = resolveEnclosingType(context);
        return typeBinding == null ? null : new IJavaCompletionProposal[] { proposalFactory.createProposal(typeBinding, enclosingType) };
    }

    @Override
    public boolean hasAssists(IInvocationContext context) throws CoreException {
        return resolveTypeBinding(context) != null;
    }

    private static ITypeBinding resolveTypeBinding(IInvocationContext context) {
        ASTNode node = context.getCoveringNode();

        if (node instanceof SimpleName) {
            ITypeBinding typeBinding = ((SimpleName) node).resolveTypeBinding();
            return resolveTypeBinding(typeBinding);
        }

        return null;
    }

    private static IType resolveEnclosingType(IInvocationContext context) {
        return context.getCompilationUnit().findPrimaryType();
    }

    static ITypeBinding resolveTypeBinding(ITypeBinding typeBinding) {
        if (typeBinding == null) {
            return null;
        }

        int modifiers = typeBinding.getModifiers();

        if (Modifier.isFinal(modifiers)) {
            return null;
        }

        return typeBinding;
    }
}
