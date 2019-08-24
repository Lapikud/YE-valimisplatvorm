# YE-valimisplatvorm

## Backend setup (Spring boot)
* Open the backend project in Intellij
* Gradle will automatically install any required dependecies
* Run Application.main()
* http://localhost:8080

## Backend requests
### Applicant parameters
Parameter | Type | Required | Details
--------- | ---- | -------- | -------
firstName | String | true | Must not be an empty string
lastName | String| true | Must not be an empty string
matrikkel | Long | true |
faculty | Enum | true |
degree | Enum | true |
phoneNumber | String | true | Must not be an empty string
email | String | true |
acceptedTerms | boolean | true | Terms must be accepted
motivationLetter | String | false | Maximum motivation letter length is 500 characters
image | Blob | false |

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
	"faculty":"INFORMATION_TECHNOLOGIES",
	"degree":"MASTERS",
	"matrikkel":184316,
	"phoneNumber":"536642358542",
	"acceptedTerms":true
}
```
