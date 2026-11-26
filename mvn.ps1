# Maven PowerShell Invoker
$mavenBin = "C:\Users\Lenovo\tools\apache-maven-3.9.16\bin\mvn.cmd"
if (Test-Path $mavenBin) {
    & $mavenBin $args
} elseif (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn $args
} else {
    Write-Error "Maven not found."
    exit 1
}
