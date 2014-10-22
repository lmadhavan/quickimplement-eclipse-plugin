package com.lmadhavan.eclipse.jdt.quickimplement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuickImplementProcessorTest {
	private static final String TYPE_NAME = "foo";
	
	@Mock private IInvocationContext context;
	@Mock private QuickImplementProposalFactory proposalFactory;
	@Mock private IJavaCompletionProposal proposal;
	@Mock private ICompilationUnit compilationUnit;
	@Mock private IType type;
	
	private AST ast;
	private IQuickAssistProcessor processor;
	
	@SuppressWarnings("deprecation")
	@Before
	public void setup() {
		this.ast = AST.newAST(AST.JLS4);
		this.processor = new QuickImplementProcessor(proposalFactory);
	}
	
	@Test
	public void appliesToTypeDeclaration() throws Exception {
		SimpleName nameNode = ast.newSimpleName(TYPE_NAME);
		TypeDeclaration typeNode = ast.newTypeDeclaration();
		typeNode.setName(nameNode);
		
		when(context.getCoveringNode()).thenReturn(nameNode);
		when(context.getCompilationUnit()).thenReturn(compilationUnit);
		when(compilationUnit.getType(TYPE_NAME)).thenReturn(type);
		when(proposalFactory.createProposal(type)).thenReturn(proposal);

		IJavaCompletionProposal[] proposals = processor.getAssists(context, null);
		assertEquals(1, proposals.length);
		assertEquals(proposal, proposals[0]);
	}
	
	@Test
	public void doesNotApplyToOtherNodes() throws Exception {
		Block blockNode = ast.newBlock();
		when(context.getCoveringNode()).thenReturn(blockNode);
		
		IJavaCompletionProposal[] proposals = processor.getAssists(context, null);
		assertNull(proposals);
	}
}
