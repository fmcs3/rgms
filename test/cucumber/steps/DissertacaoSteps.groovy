import steps.TestDataAndOperations
import pages.LoginPage
import pages.PublicationsPage
import pages.DissertationPage
import pages.DissertationCreate
import pages.DissertationShowPage
import pages.DissertationEditPage
import rgms.publication.Dissertacao

import static cucumber.api.groovy.EN.*

When(~'^I select the "([^"]*)" option at the program menu$') { String option ->
    page.select(option)
}

When(~'^I select the new dissertation option at the dissertation page$') {->
    at DissertationPage
    page.selectNewArticle()
}

When(~'^I cant add the dissertation without a file$') {->
    at DissertationCreate
    page.fillDissertationDetailsWithoutFile()
}



When(~'^I select "([^"]*)" at the dissertation page$') {String title ->
    at DissertationPage
    page.selectDissertation(title)
}

When(~'^I click on edit$'){->
    at DissertationShowPage
    page.editDissertation()
}

When(~'^I delete it$'){->
    at DissertationShowPage
    page.deleteDissertation()
}

Then(~'^the school name is "([^"]*)"$'){ String name->
    page.nameIs(name)
}

When(~'^I edit the school to "([^"]*)"$'){String school ->
    at DissertationEditPage
    page.editSchool(school)
}

Given(~'^the system has no dissertation entitled "([^"]*)"$') { String title ->
    article = Dissertacao.findByTitle(title)
    assert article == null
}

Given(~'^the dissertation "([^"]*)" is stored in the system with file name "([^"]*)"$') { String title, filename ->
	TestDataAndOperations.createDissertacao(title, filename, "UFPE")
	article = Dissertacao.findByTitle(title)
    assert article != null
}


When(~'^I create the dissertation "([^"]*)" with file name "([^"]*)" and school "([^"]*)"$') { String title, filename, school ->
    TestDataAndOperations.createDissertacao(title, filename, school)
}


Then(~'^the dissertation "([^"]*)" is properly stored by the system$') { String title ->
    dissertation = Dissertacao.findByTitle(title)
    assert dissertation != null
}

Then(~'^the dissertation "([^"]*)" is not stored twice$') { String title ->
    dissertations = Dissertacao.findAllByTitle(title)
    assert dissertations.size() == 1
    //A propriedade title de publication deveria estar unique:true, mas como n�o est�, este teste vai falhar
}

When(~'^I create the dissertation "([^"]*)" with file name "([^"]*)" without school$') { String title, filename ->
    TestDataAndOperations.createDissertacaoWithotSchool(title, filename);
}

When(~'^I edit the dissertation title from "([^"]*)" to "([^"]*)"$') { String oldtitle, newtitle ->
	def updatedDissertation = TestDataAndOperations.editDissertatacao(oldtitle, newtitle)
	assert updatedDissertation != null
}

Then(~'^the dissertation "([^"]*)" is properly updated by the system$') { String title ->
	def article = Dissertacao.findByTitle(title)
	assert article == null
}

When(~'^I select the upload button at the dissertation page$') { ->
    at DissertationPage
    page.uploadWithoutFile()
}
Then(~'^I\'m still on dissertation page$') {  ->
    at DissertationPage
}
Given(~'^the system has some dissertation stored$') { ->
    inicialSize = Dissertacao.findAll().size()
	
}
When(~'^I upload a new dissertation "([^"]*)"$') {  filename ->
    inicialSize = Dissertacao.findAll().size()
    TestDataAndOperations.uploadDissertacao(filename)
    finalSize = Dissertacao.findAll().size()
    assert inicialSize<finalSize
    //para funcionar é necessario que tenha um FilePath válido
    // não consegui fazer de uma maneira que todos os passos sejam independentes
}
Then(~'the system has more dissertations now$') {->
    finalSize = Dissertacao.findAll().size()

}