# Mobile Recharge Using Alexa
A simple [AWS Lambda](http://aws.amazon.com/lambda) function that demonstrates how to do mobile recharge by pay2all wallet using the Amazon Alexa.

## Concepts
As the technology is continue evolving, things are moving from Web to Mobile App and now on Voice commands. We use mobile recharge applications on smarphones for recharge. In this example, we will be using our voice as a command to alexa for processing the mobile recharge request.

## Setup
To run this example skill you need to do two things. The first is to deploy the example code in lambda, and the second is to configure the Alexa skill to use Lambda.

### AWS Lambda Setup
1. Go to the AWS Console and click on the Lambda link. Note: ensure you are in us-east or you wont be able to use Alexa with Lambda.
2. Click on the Create a Lambda Function or Get Started Now button.
3. Skip the blueprint
4. Configure Triggers Screen click the outlined empty square and select Alexa Skill Kit.  Click Next
5. Name the Lambda Function "AlexaMobileRecharge".
6. Select the runtime as Java 8
7. Go to the the root directory containing pom.xml, and run 'mvn assembly:assembly -DdescriptorId=jar-with-dependencies package'. This will generate a jar file named "AlexaMobileRecharge-0.0.1-SNAPSHOT-jar-with-dependencies.jar" in the target directory.
8. Select Code entry type as "Upload a .ZIP file" and then upload the "AlexaMobileRecharge-0.0.1-SNAPSHOT-jar-with-dependencies.jar" file from the build directory to Lambda
9. Set the Handler as com.recharge.RechargeSpeechletRequestStreamHandler (this refers to the Lambda RequestStreamHandler file in the jar).
10. Create a basic execution role and click create.
11. Leave the Advanced settings as the defaults.
12. Click "Next" and review the settings then click "Create Function"
13. Copy the ARN from the top right to be used later in the Alexa Skill Setup.

### Alexa Skill Setup
1. Go to the [Alexa Console](https://developer.amazon.com/edw/home.html) and click Add a New Skill.
2. Set "AlexaMobileRecharge" as the skill name and "pda" as the invocation name, this is what is used to activate your skill.
3. Select the Lambda ARN for the skill Endpoint and paste the ARN copied from above. Click Next.
4. Copy the custom slot types from the speechAssets folder. Files LIST_OF_Numbers and Confirmation_Type represents a new custom slot type. The name of the file is the name of the custom slot type, and the values in the file are the values for the custom slot.
5. Copy the Intent Schema from the included IntentSchema.json.
6. Copy the Sample Utterances from the included SampleUtterances.txt. Click Next.
7. Go back to the skill Information tab and copy the appId. Paste the appId into the RechargeSpeechletRequestStreamHandler.java file for the variable supportedApplicationIds, then update the lambda source zip file with this change and upload to lambda again, this step makes sure the lambda function only serves request from authorized source.
7. You are now able to start testing your sample skill! You should be able to go to the [Echo webpage](http://echo.amazon.com/#skills) and see your skill enabled.
8. In order to test it, try to say some of the Sample Utterances from the Examples section below.
9. Your skill is now saved and once you are finished testing you can continue to publish your skill.

## Examples
### One-shot model:
    User: "Hello Alexa, launch PDA"
    Alexa: "Welcome to the Voice Recharge Service, Say Hello"
	User: "Hello"
	Alexa: "Your 10 Digit Mobile Number Please"
	User: "1234567890"