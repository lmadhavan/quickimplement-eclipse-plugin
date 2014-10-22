package com.lmadhavan.eclipse.jdt.quickimplement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.wizards.NewClassWizardPage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuickImplementChangeTest {
	private static final String NAME = "foo";
	private static final String FULLY_QUALIFIED_NAME = "a.b.foo";
	
	@Mock private IType type;
	@Mock private IPackageFragment packageFragment;
	@Mock private IPackageFragmentRoot packageFragmentRoot;
	@Mock private NewClassWizardPage page;
	
	private QuickImplementChange change;
	
	@Before
	public void setup() {
		when(type.getElementName()).thenReturn(NAME);
		when(type.getFullyQualifiedName()).thenReturn(FULLY_QUALIFIED_NAME);
		
		when(type.getPackageFragment()).thenReturn(packageFragment);
		when(packageFragment.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT)).thenReturn(packageFragmentRoot);
		
		this.change = new QuickImplementChange(type);
	}
	
	@Test
	public void setsDefaults() throws Exception {
		change.configureNewClassWizardPage(page);

		verify(page).setMethodStubSelection(false, false, true, true);
		verify(page).setEnclosingType(type, true);
		verify(page).setPackageFragment(packageFragment, true);
		verify(page).setPackageFragmentRoot(packageFragmentRoot, true);
	}
	
	@Test
	public void appliesToInterface() throws Exception {
		when(type.isInterface()).thenReturn(true);
		
		change.configureNewClassWizardPage(page);

		assertEquals(String.format(QuickImplementChange.IMPLEMENT_TEXT, NAME), change.getName());
		verify(page).setSuperInterfaces(Arrays.asList(FULLY_QUALIFIED_NAME), true);
	}
	
	@Test
	public void appliesToClass() throws Exception {
		change.configureNewClassWizardPage(page);

		assertEquals(String.format(QuickImplementChange.EXTEND_TEXT, NAME), change.getName());
		verify(page).setSuperClass(FULLY_QUALIFIED_NAME, true);
	}
}
