package com.lmadhavan.eclipse.jdt.quickimplement;

import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.actions.OpenNewClassWizardAction;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

/**
 * {@link Change} object that launches a New Class wizard to implement/extend a Java type.
 */
public class QuickImplementChange extends Change {
	static final String IMPLEMENT_TEXT = "Implement '%s'";
	static final String EXTEND_TEXT = "Extend '%s'";
	
	private final IType type;
	
	public QuickImplementChange(IType type) {
		this.type = type;
	}

	@Override
	public Object getModifiedElement() {
		return null;
	}

	@Override
	public String getName() {
		try {
			return String.format(type.isInterface() ? IMPLEMENT_TEXT : EXTEND_TEXT, type.getElementName());
		} catch (JavaModelException e) {
			return "";
		}
	}

	@Override
	public void initializeValidationData(IProgressMonitor pm) {
	}

	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		return new RefactoringStatus();
	}
	
	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		NewClassWizardPage page = new NewClassWizardPage();
		configureNewClassWizardPage(page);
		
		OpenNewClassWizardAction action = new OpenNewClassWizardAction();
		action.setConfiguredWizardPage(page);
		action.run();
		return null;
	}
	
	void configureNewClassWizardPage(NewClassWizardPage page) throws JavaModelException {
		page.setMethodStubSelection(false, false, true, true);
		page.setEnclosingType(type, true);
		page.setPackageFragment(type.getPackageFragment(), true);
		page.setPackageFragmentRoot((IPackageFragmentRoot) type.getPackageFragment().getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT), true);
		
		if (type.isInterface()) {
			page.setSuperInterfaces(Arrays.asList(type.getFullyQualifiedName()), true);
		} else {
			page.setSuperClass(type.getFullyQualifiedName(), true);
		}
	}
}
