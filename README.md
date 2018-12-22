# YE-valimisplatvorm

## Backend setup (Spring boot)
* Open the backend project in Intellij
* Gradle will automatically install any required dependecies
* Run Application.main()
* http://localhost:8080

## Backend requests
### Applicant form parameters
Parameter | Type | Required | Details
--------- | ---- | -------- | -------
firstName | String | true | Must not be an empty string
lastName | String| true | Must not be an empty string
matrikkel | String | true | Must not be an empty string
faculty | String | true | Must not be an empty string
major | String | true | Must not be an empty string
phoneNumber | String | true | Must not be an empty string
email | String | true | Must not be an empty string
acceptedTerms | boolean | true | Terms must be accepted
motivationLetter | String | false | Maximum motivation letter length is 500 characters
image | BufferedImage | false |

### Generate PDF
POST request http://localhost:8080/generate

Request content type: JSON

Response content type: application/pdf

request example
```json
{
	"firstName":"John",
	"lastName":"Smith",
	"email":"jsmith@taltech.ee",
	"faculty":"IT-teaduskond",
	"major":"Informaatika",
	"matrikkel":"184316IAPB",
	"phoneNumber":"53668542",
	"acceptedTerms":true
}
```
